package com.human.dalligo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.human.dalligo.dao.LshEventDAO;
import com.human.dalligo.dao.LshTripDAO;
import com.human.dalligo.vo.LshApplyListVO;
import com.human.dalligo.vo.LshApplyVO;
import com.human.dalligo.vo.LshCityVO;
import com.human.dalligo.vo.LshDistanceVO;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripSumVO;
import com.human.dalligo.vo.LshTripVO;
import com.human.dalligo.vo.LshUserVO;

import lombok.Data;
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
        LshDistanceVO cityDist = distanceService.getDistance(startCity, endCity);
        BigDecimal distance = (cityDist != null && cityDist.getDistanceKm() != null) 
                               ? cityDist.getDistanceKm() 
                               : BigDecimal.ZERO;
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
        
        // ★ LocalDate → Timestamp 변환 적용
//        LocalDate localDate = event.getStartDate().toLocalDate(); // LocalDate 추출
//        Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay()); // 00:00 시각 붙여서 Timestamp로 변환
//        trip.setTripDate(timestamp); // VO 메소드와 타입 일치
        //trip.setTripDate(event.getStartDate().toLocalDate());
        //trip.setTripTime(LocalTime.of(6,0)); // 기본 출발시간 06:00
        // status 상태 계산 적용
        String status = computeStatus(tripDate, 0);
        trip.setStatus(status);

        tripDAO.insertTrip(trip);
        return tripDAO.getTripByEvent(event.getId());
    }


    /** Trip 신규 생성/거리 업데이트 */
//    @Transactional
//    public LshTripVO updateOrCreateTripWithDistance(int eventId, String userId, BigDecimal distance) {
//
//        LshTripVO trip = getTripByEvent(eventId);
//
//        if (trip == null) {
//            LshEventVO ev = eventDAO.selectOne(eventId);

//            trip = new LshTripVO();
//            trip.setEventId(eventId);
//            trip.setUserId(userId);
//            trip.setStartCity("서울"); 
//
//            String endCity = extractCity(ev.getLocation());
//            trip.setEndCity(endCity);

//            trip.setDistance(distance);
//            trip.setCost(calculateCost(distance));
//            trip.setCurrentPeople(0);
//            // ★ LocalDate → Timestamp 변환 적용
//            LocalDate localDate = ev.getStartDate().toLocalDate(); // LocalDate 추출
//            Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay()); // 00:00 시각 붙여서 Timestamp로 변환
//            trip.setTripDate(timestamp); // VO 메소드와 타입 일치
            // ⭐ 상태 다시 계산
//            String status = computeStatus(trip.getTripDate(), trip.getCurrentPeople());
//            trip.setStatus(status);
//
//            tripDAO.insertTrip(trip);
//
//        } else {
//            trip.setDistance(distance);
//            trip.setCost(calculateCost(distance));
//            tripDAO.updateTrip(trip);
//        }
//
//        return trip;
//    }

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

    public int getApplicationCount(int eventId) {
        return tripDAO.countApplicationsByEvent(eventId);
    }


    
    // 게시판 신청인원 합산
//    public List<LshTripSumVO> getTripSumByRoute() {
//    	return tripDAO.selectGroupedTripStatus();
//	}
//    
//    public boolean cancelApplication(int eventId, String userId) {
//    	int rows = tripDAO.deleteTripApplication(eventId, userId);
//        return rows > 0;
//	}

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
    public int calculateCost(BigDecimal distanceKm) {
        if (distanceKm == null) return 28000;
        double km = distanceKm.doubleValue();
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
        String newStatus = computeStatus(updated.getTripDate(), updated.getCurrentPeople());

        // trips 테이블 상태 업데이트
        tripDAO.updateTripStatus(eventId, newStatus);

        // application 상태 저장
        tripDAO.updateApplicationStatus(userId, eventId, "신청");
        
        // ★ 여기 추가!!
        int count = tripDAO.countApplicationsByEvent(eventId);
        tripDAO.updateCurrentPeople(eventId, count);

        return 1;
    }

    
    /** Trip 신청 */
//    @Transactional
//    public void applyToEvent(int eventId, String userId) {
//
//        // 1) 중복 신청 확인
//        int exists = tripDAO.existsApplication(userId, eventId);
//        if (exists > 0) {
//            throw new IllegalStateException("이미 신청하셨습니다.");
//        }
//
//        // 2) 신청 (application status는 "신청"으로 기록)
//        LshApplyVO vo = new LshApplyVO();
//        vo.setUserId(userId);
//        vo.setEventId(eventId);
//        vo.setStatus("신청");
//        tripDAO.insertApplication(vo);
//
//        // 3) trips.current_people 증가 (DB의 현재값과 동기화)
//        tripDAO.incrementTripCurrentPeople(eventId);
//
//        // 4) event.start_date -> trip.trip_date
//        LshEventVO event = eventDAO.selectOne(eventId);
//        
//        if (event == null) {
//            throw new RuntimeException("Event not found: " + eventId);
//        }
//
//        // 2. trip 조회
//        LshTripVO trip = tripDAO.getTripByEvent(eventId);
//        if (trip == null) {
//            throw new RuntimeException("Trip not found for eventId: " + eventId);
//        }
//        
//        trip.setTripDate(event.getStartDate().toLocalDate());
//        tripDAO.updateTrip(trip);
//        
//        if (trip == null) {
//            // 만약 trip이 없으면 상황에 맞게 처리 (예외 또는 로그)
//            throw new IllegalStateException("해당 이벤트에 대한 trip 정보가 없습니다.");
//        }
//
//        // 5) 오늘 날짜와 trip_date(날짜 타입) 기준으로 상태 결정
//        LocalDate today = LocalDate.now();
//        LocalDate tripDate = trip.getTripDate(); // LshTripVO 의 tripDate는 java.time.LocalDate 이어야 함
//
//        String newTripStatus;
//        if (today.isBefore(tripDate)) {
//            newTripStatus = "모집중";
//        } else {
//            // 오늘이거나 이후일 경우 currentPeople 기준
//            int currentPeople = trip.getCurrentPeople();
//            if (currentPeople >= 25) {
//                newTripStatus = "승인";
//            } else {
//                newTripStatus = "종료";
//            }
//        }
//
//        // 6) trips.status 업데이트 (필요 시에만 수행)
//        if (!newTripStatus.equals(trip.getStatus())) {
//            tripDAO.updateTripStatus(eventId, newTripStatus);
//        }
//    }
    
 // ----- 신청: 중복 체크 후 insert (event 기준) -----
    @Transactional
    public int applyToEvent(int eventId, String userId) {
        // 1) 중복 체크
        int exists = tripDAO.existsApplication(userId, eventId);
        if (exists > 0) {
            return -1; // 이미 신청되어 있으면 insert/증가 모두 금지
        }
        System.out.println("applyToEvent called: eventId=" + eventId + ", userId=" + userId);
        
        // 다른 방식의 중복 체크
        //int count = tripDAO.countExistingApplication(userId, eventId);
        //if (count > 0) {
        //    return -1; // 이미 신청됨
        //}

        // 3) trips.current_people 같은 컬럼 직접 변경하지 않음 — count(*)로 관리
        return applyToTrip(eventId, userId);
    }
    
    // ----- 신청 취소 -----
    @Transactional
    public boolean cancelApplication(int eventId, String userId) {
        int rows = tripDAO.deleteTripApplication(eventId, userId);
        // rows > 0 이면 삭제 성공
        return rows > 0;
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
        
        for (String p : parts) {
            for (String city : majorCities) {
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
        return tripDAO.selectAllApplicationsWithEvent();
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

}
//        for (Map<String, Object> r : rows) {
//        	LshApplyListVO a = new LshApplyListVO();
//            a.setApplyId((Integer) r.get("apply_id"));
//            a.setStartCity((String) r.get("start_city"));
//            a.setEndCity((String) r.get("end_city"));
//            a.setTitle((String) r.get("title"));
//            // Timestamp → null 발생 가능 대비
//            Object dateObj = r.get("start_date");
//            a.setDate(dateObj instanceof Timestamp ? (Timestamp) dateObj : null);
//            // current_people → Long 또는 Integer 주의
//            Object countObj = r.get("current_people");
//            if (countObj instanceof Integer i)     a.setApplyCount(i);
//            else if (countObj instanceof Long l)   a.setApplyCount(l.intValue());
//            else                                   a.setApplyCount(0);
//            a.setStatus((String) r.get("status"));
//            out.add(a);
//        }
//        return out;
//  }
//}

