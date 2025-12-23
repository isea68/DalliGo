package com.human.dalligo.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.human.dalligo.dao.LshEventDAO;
import com.human.dalligo.dao.LshTripDAO;
import com.human.dalligo.vo.LshApplyListVO;
import com.human.dalligo.vo.LshApplyVO;
import com.human.dalligo.vo.LshCityVO;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripVO;
import com.human.dalligo.vo.LshUserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LshTripService {

    private final LshTripDAO tripDAO;
    private final LshEventDAO eventDAO;
    private final LshEventService eventService;
    private final LshUserService userService;
    private final LshDistanceService distanceService;

    /** 이벤트별 Trip (첫 번째 Trip 반환) */
    public LshTripVO getTripByEvent(int eventId) {
    	// DAO에 getTripByEvent가 있으므로 직접 호출 
    	return tripDAO.getTripByEvent(eventId);
    }
    
    // 거리 업데이트 : trip객체에 distance를 같이 넘겨야 함
    public LshTripVO getTripById(int tripId) {
        return tripDAO.getTripById(tripId); // DAO에 이미 구현되어 있어야 함
    }

    public List<LshTripVO> getTripsByEvent(int eventId) {
        return tripDAO.selectTripsByEvent(eventId);
    }

    public LshTripVO getTrip(int tripId) {
        return tripDAO.selectTrip(tripId);
    }

    public LshCityVO getCity(String cityName) {
        return tripDAO.selectCity(cityName);
    }
    
    public LshTripVO getTripByUserAndEvent(String userId, int eventId) {
        return tripDAO.selectTripByUserAndEvent(userId, eventId);
    }

    @Transactional
    public LshTripVO createTripFromEvent(LshEventVO event, String userId) {
        LshTripVO trip = new LshTripVO();
        trip.setEventId(event.getId());
        trip.setUserId(userId);
        // 🔥 바로 여기에 날짜를 넣어야 함
        LocalDateTime startDateTime = event.getStartDate(); // LocalDateTime
        LocalDate localDate = startDateTime.toLocalDate();  // LocalDate
        LocalTime localTime = startDateTime.toLocalTime();  // LocalTime
        
        // TripVO 에 맞게 변환
        Timestamp tripDate = Timestamp.valueOf(localDate.atStartOfDay());
        trip.setTripDate(tripDate);
        trip.setTripTime(localTime); // LocalTime → 그대로 넣으면 됨
        
        // userService를 통해 user 객체 가져오기
        LshUserVO user = userService.getUserByUserId(userId);
        // user.address 가져오기
        String userAddress = user.getAddress();
        String startCity = extractCity(userAddress);
        trip.setStartCity(startCity); // 출발지는 항상 서울
        // ⭐ 도착지는 event.location에서 매번 새로 추출
        String endCity = extractCity(event.getLocation());
        trip.setEndCity(endCity);
        //trip.setEndCity(extractCity(event.getLocation()));

        // ★ 여기에 거리/비용 계산 추가 ★
        Integer distKm = distanceService.getDistance(startCity, endCity);
        // Integer → BigDecimal 변환하여 trip에 저장
        BigDecimal distance = BigDecimal.valueOf(distKm);
        trip.setDistance(distance);
        
        // 로그 확인용
        System.out.println("=== 거리 계산 ===");
        System.out.println("출발지: " + startCity);
        System.out.println("도착지: " + endCity);
        System.out.println("거리: " + distance);


        int cost = calculateCost(distance);
        // 제주 예외 처리: start 또는 end가 제주면 한 번만 추가
        if ("제주".equals(startCity) || "제주".equals(endCity)) {
            cost += 110_000;
        }
        trip.setCost(cost);
        
        trip.setCurrentPeople(0);
        
        // status 상태 계산 적용
        String status = computeStatus(tripDate, 0);
        trip.setStatus(status);

        tripDAO.insertTrip(trip);
        return tripDAO.getTripByEvent(event.getId());
    }

    /** 신청 */
    @Transactional
    public boolean applyEvent(String userId, int eventId) {

        int exists = tripDAO.existsApplication(userId, eventId);
        if (exists > 0) {
            return false; // 중복 신청 방지
        }

        tripDAO.insertApply(userId, eventId);
        return true;
    }
    
    // 특정 이벤트의 현재 신청자 수 조회
    public int getApplicationCount(int eventId) {
        return tripDAO.countApplicationsByEvent(eventId);
    }

    // status 상태 계산
    private String computeStatus(Timestamp startDate, int currentPeople) {
    	if (startDate == null) {
            return "모집중";  // 기본값
        }
    	
        LocalDate today = LocalDate.now();
        LocalDate eventDay = startDate.toLocalDateTime().toLocalDate();

        if (today.isBefore(eventDay)) {
            return "모집중";
        } else if (today.isEqual(eventDay)) {
            return (currentPeople >= 25) ? "출발확정" : "종료";
        } else {
            return "신청불가";
        }
    }


    /** 요금 계산 */
    public int calculateCost(BigDecimal distance) {
        if (distance == null) return 28000;
        double km = distance.doubleValue()*2; // 왕복처리
        if (km <= 100) return 28000;
        if (km <= 300) return 40000;
        return 52000;
    }
    
    // 실제 trip_applications 테이블에 insert하는 서비스
    @Transactional
    public int applyToTrip(int eventId, String userId) {

        // eventId 기반으로 trip 조회
        LshTripVO trip = tripDAO.getTripByEvent(eventId);
        if (trip == null) return 0;

        // INSERT
        LshApplyVO app = new LshApplyVO();
        app.setUserId(userId);
        app.setEventId(eventId);
        app.setStatus("신청");
        tripDAO.insertApplication(app);

        // 최신값 조회
        LshTripVO updated = tripDAO.getTripByEvent(eventId);

        // 상태 계산
        int count = tripDAO.countApplicationsByEvent(eventId);
        tripDAO.updateCurrentPeople(eventId, count);

        String newStatus = computeStatus(updated.getTripDate(), count);
        tripDAO.updateTripStatus(eventId, newStatus);


        // trips 테이블 상태 업데이트
        tripDAO.updateTripStatus(eventId, newStatus);

        // application 상태 저장
        tripDAO.updateApplicationStatus(userId, eventId, "신청");
        
        return 1;
    }
    
 // ----- 신청: 중복 체크 후 insert (event 기준) -----
    @Transactional
    public int applyToEvent(int eventId, String userId) {
        // 1) 중복 체크
        int exists = tripDAO.existsApplication(userId, eventId);
        if (exists > 0) {
            return -1; // 이미 신청되어 있으면 insert/증가 모두 금지
        }
        System.out.println("applyToEvent called: eventId=" + eventId + ", userId=" + userId);

        // 3) trips.current_people 같은 컬럼 직접 변경하지 않음 — count(*)로 관리
        return applyToTrip(eventId, userId);
    }
    
    // ----- 신청 취소 -----
    @Transactional
    public boolean cancelApplication(int eventId, String userId) {
        int rows = tripDAO.deleteTripApplication(eventId, userId);
        // rows > 0 이면 삭제 성공
        if (rows == 0) return false;
        
     // 최신 인원수
        int count = tripDAO.countApplicationsByEvent(eventId);
        tripDAO.updateCurrentPeople(eventId, count);

        // 상태 재계산
        LshTripVO trip = tripDAO.getTripByEvent(eventId);
        if (trip != null) {
            String status = computeStatus(trip.getTripDate(), count);
            tripDAO.updateTripStatus(eventId, status);
        }

        return true;
    }
    
    public int getCurrentPeople(int eventId) {
        return tripDAO.countApplicationsByEvent(eventId);
    }

    /** 도시명 추출 */
    public String extractCity(String address) {
        if (address == null || address.isEmpty()) return "수원";

        // 1) 광역시 / 특별시 / 특별자치시 / 특별자치도
        String[] cityPrefixes = {
            "서울특별시", "부산광역시", "대구광역시", "인천광역시",
            "광주광역시", "대전광역시", "울산광역시",
            "세종특별자치시", "제주특별자치도"
        };

        for (String prefix : cityPrefixes) {
            if (address.contains(prefix)) {
                // 예: "대전광역시" → "대전"
                return prefix.replace("특별시", "")
                             .replace("광역시", "")
                             .replace("특별자치시", "")
                             .replace("특별자치도", "")
                             .replace("시", "")
                             .trim();
            }
        }

        // 2) "OO도 OO시" 형태 처리
        String[] parts = address.split("\\s+");
        for (String part : parts) {
            if (part.endsWith("시")) {
                return part.substring(0, part.length() - 1);
            }
        }
        
        List<String> majorCities = List.of(
                "서울", "부산", "대구", "광주", "인천", "대전", "울산",
                "수원", "전주", "청주", "안동", "포항", "춘천", "강릉", 
                "속초", "목포", "구미", "포천", "제주", "성남", "용인"
        );
        
        for (String city : majorCities) {
            if (address.startsWith(city)) {
                return city;
            }
            for (String p : parts) {
                if (p.contains(city)) {
                    return city;
                }
            }
        }

        // 기본 fallback
        return "수원";
    }

    /** 신청 목록 조회 */
    public List<LshApplyListVO> getAllApplicationsWithEventInfo() {
    	List<LshApplyListVO> list = tripDAO.selectAllApplicationsWithEvent();

        for (LshApplyListVO vo : list) {

            Timestamp tripDate = vo.getDate();          // 일정
            int currentPeople = vo.getApplyCount();     // 신청 인원

            String recalculatedStatus = computeStatus(tripDate, currentPeople);

            vo.setStatus(recalculatedStatus);            // ⭐ 덮어쓰기
        }

        return list;
    }
    
    // trip_applications 테이블에 튶플 생성시 trips테이블의 current_people를 업데이트함
    public void applyTrip(String userId, int eventId) {

        // 1) 신청 저장
        tripDAO.insertApply(userId, eventId);

        // 2) 카운트 조회
        int count = tripDAO.countApplicationsByEvent(eventId);

        // 3) current_people 업데이트
        tripDAO.updateCurrentPeople(eventId, count);
    }
    
    @Transactional
    public LshTripVO updateOrCreateTripWithDistance(int eventId, String userId, BigDecimal distance) {
        LshTripVO trip = getTripByEvent(eventId);

        if (trip == null) {
            LshEventVO ev = eventDAO.selectOne(eventId);
            trip = new LshTripVO();
            trip.setEventId(eventId);
            trip.setUserId(userId);

            LshUserVO user = userService.getUserByUserId(userId);
            String startCity = extractCity(user.getAddress());
            String endCity = extractCity(ev.getLocation());
            trip.setStartCity(startCity);
            trip.setEndCity(endCity);

            trip.setDistance(distance);

            int cost = calculateCost(distance);
            if ("제주".equals(startCity) || "제주".equals(endCity)) {
                cost += 110_000;
            }
            trip.setCost(cost);

            trip.setCurrentPeople(0);
            Timestamp tripDate = Timestamp.valueOf(ev.getStartDate().toLocalDate().atStartOfDay());
            trip.setTripDate(tripDate);

            String status = computeStatus(trip.getTripDate(), trip.getCurrentPeople());
            trip.setStatus(status);

            tripDAO.insertTrip(trip);

        } else {
            trip.setDistance(distance);

            int cost = calculateCost(distance);
            if ("제주".equals(trip.getStartCity()) || "제주".equals(trip.getEndCity())) {
                cost += 110_000;
            }
            trip.setCost(cost);

            tripDAO.updateTrip(trip);
        }

        return trip;
    }

	public String getCityAddress(String cityName) {
		
		if (cityName == null || cityName.isBlank()) {
            return "&&&&&";
        }

        LshCityVO city = tripDAO.selectCity(cityName.trim());

        if (city == null) {
            return "@@@@@@@";
        }

	    return city.getCityAddr();
	}
	
	public String getCityPlace(String cityName) {
		LshCityVO city = tripDAO.selectCity(cityName);
		if (city == null) {
	        return null;
	    }
		return city.getCityPlace();
	}

}

