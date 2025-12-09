package com.human.dalligo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.sql.Timestamp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.human.dalligo.dao.LshEventDAO;
import com.human.dalligo.dao.LshTripDAO;
import com.human.dalligo.vo.LshApplyVO;
import com.human.dalligo.vo.LshCityVO;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripSumVO;
import com.human.dalligo.vo.LshTripVO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LshTripService {

    private final LshTripDAO tripDAO;
    private final LshEventDAO eventDAO;
    private final LshEventService eventService;

    /** 이벤트별 Trip (첫 번째 Trip 반환) */
    public LshTripVO getTripByEvent(int eventId) {
        List<LshTripVO> list = tripDAO.selectTripsByEvent(eventId);        
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    public List<LshTripVO> getTripsByEvent(int Id) {
        return tripDAO.selectTripsByEvent(Id);
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
        trip.setStartCity("서울"); // 출발지는 항상 서울
        // ⭐ 도착지는 event.location에서 매번 새로 추출
        String endCity = extractCity(event.getLocation());
        trip.setEndCity(endCity);
        //trip.setEndCity(extractCity(event.getLocation()));

        trip.setDistance(BigDecimal.valueOf(0.0));
        trip.setCost(0);
        trip.setCurrentPeople(0);
        // ★ LocalDate → Timestamp 변환 적용
        LocalDate localDate = event.getStartDate().toLocalDate(); // LocalDate 추출
        Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay()); // 00:00 시각 붙여서 Timestamp로 변환
        trip.setTripDate(timestamp); // VO 메소드와 타입 일치
        //trip.setTripDate(event.getStartDate().toLocalDate());
        //trip.setTripTime(LocalTime.of(6,0)); // 기본 출발시간 06:00
        trip.setStatus("모집중");

        tripDAO.insertTrip(trip);
        return trip;
    }


    /** Trip 신규 생성/거리 업데이트 */
    @Transactional
    public LshTripVO updateOrCreateTripWithDistance(int eventId, String userId, BigDecimal distance) {

        LshTripVO trip = getTripByEvent(eventId);

        if (trip == null) {
            LshEventVO ev = eventDAO.selectOne(eventId);

            trip = new LshTripVO();
            trip.setEventId(eventId);
            trip.setUserId(userId);
            trip.setStartCity("서울"); 

            String endCity = extractCity(ev.getLocation());
            trip.setEndCity(endCity);

            trip.setDistance(distance);
            trip.setCost(calculateCost(distance));
            trip.setCurrentPeople(0);
            // ★ LocalDate → Timestamp 변환 적용
            LocalDate localDate = ev.getStartDate().toLocalDate(); // LocalDate 추출
            Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay()); // 00:00 시각 붙여서 Timestamp로 변환
            trip.setTripDate(timestamp); // VO 메소드와 타입 일치
            trip.setStatus("모집중");

            tripDAO.insertTrip(trip);

        } else {
            trip.setDistance(distance);
            trip.setCost(calculateCost(distance));
            tripDAO.updateTrip(trip);
        }

        return trip;
    }

    /** 신청 */
    @Transactional
    public int apply(String userId, int tripId) {

        LshTripVO trip = tripDAO.selectTrip(tripId);
        if (trip == null) return 0;

        LshApplyVO app = new LshApplyVO();
        app.setUserId(userId);
        app.setEventId(trip.getEventId());
        app.setStatus("신청");

        tripDAO.insertApplication(app);
        tripDAO.increaseCurrentPeople(tripId);

        return 1;
    }
    
    // 게시판 신청인원 합산
    public List<LshTripSumVO> getTripSumByRoute() {
    	return tripDAO.selectGroupedTripStatus();
	}
    
    public boolean cancelApplication(int tripId, String userId) {
    	int rows = tripDAO.deleteTripApplication(tripId, userId);
        return rows > 0;
	}

    /** 상태 계산 */
    public String computeStatus(LshTripVO trip, LshEventVO event) {

        // 현재 시각을 Timestamp로
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 이벤트 시작일을 Timestamp로 변환 (00:00 시각 기준)
        Timestamp deadline = Timestamp.valueOf(event.getStartDateOnly().atStartOfDay());

        // 최소 출발 인원(고정값)
        final int MIN_PEOPLE = 25;

        // 이벤트 시작일이 지난 경우
        if (now.compareTo(deadline) >= 0) { // now >= deadline
            return trip.getCurrentPeople() >= MIN_PEOPLE ? "출발확정" : "취소";
        }

        // 이벤트 시작일 이전 + 최소 인원 충족
        if (trip.getCurrentPeople() >= MIN_PEOPLE) {
            return "출발확정";
        }

        // 기본 상태
        return "모집중";
    }


    /** 요금 계산 */
    public int calculateCost(BigDecimal distanceKm) {
        if (distanceKm == null) return 28000;
        double km = distanceKm.doubleValue();
        if (km <= 100) return 28000;
        if (km <= 300) return 40000;
        return 52000;
    }
    
    @Transactional
    public int applyToTrip(int tripId, String userId) {
        LshTripVO trip = getTrip(tripId);
        if (trip == null) return 0;

        LshApplyVO app = new LshApplyVO();
        app.setUserId(userId);
        app.setEventId(trip.getEventId());
        app.setStatus("신청");

        tripDAO.insertApplication(app);
        tripDAO.increaseCurrentPeople(tripId);

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
    
    @Transactional
    public boolean applyToEvent(int eventId, String userId) {
        int exists = tripDAO.existsApplication(userId, eventId);
        if (exists > 0) return false; // 이미 신청

        // 신규 신청
        LshApplyVO vo = new LshApplyVO();
        vo.setUserId(userId);
        vo.setEventId(eventId);
        vo.setStatus("신청");
        tripDAO.insertApplication(vo);

        // trips.current_people 증가
        tripDAO.incrementTripCurrentPeople(eventId);

        return true;
    }
    
    public int getCurrentPeople(int eventId) {
        LshTripVO trip = tripDAO.getTripByEvent(eventId);
        return trip.getCurrentPeople();
    }

    /** 도시명 추출 */
    public String extractCity(String location) {
        if (location == null || location.isEmpty()) return "";

        // 1) 광역시 / 특별시 / 특별자치시 / 특별자치도
        String[] cityPrefixes = {
            "서울특별시", "부산광역시", "대구광역시", "인천광역시",
            "광주광역시", "대전광역시", "울산광역시",
            "세종특별자치시", "제주특별자치도"
        };

        for (String prefix : cityPrefixes) {
            if (location.startsWith(prefix)) {
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
        String[] parts = location.split("\\s+");
        for (String part : parts) {
            if (part.endsWith("시")) {
                return part.substring(0, part.length() - 1);
            }
        }

        // 3) 기본 fallback
        return parts[0].replace("시", "");
    }

    /** 신청 목록 조회 */
    public List<ApplicationDTO> getAllApplicationsWithEventInfo() {

        List<Map<String, Object>> rows = tripDAO.selectAllApplicationsWithEvent();
        List<ApplicationDTO> out = new ArrayList<>();

        for (Map<String, Object> r : rows) {
            ApplicationDTO a = new ApplicationDTO();
            a.setApplyId((Integer) r.get("apply_id"));
            a.setStartCity((String) r.get("start_city"));
            a.setEndCity((String) r.get("end_city"));
            a.setTitle((String) r.get("title"));
            a.setDate((Timestamp) r.get("start_date"));
            a.setApplyCount((Integer) r.get("current_people"));
            a.setStatus((String) r.get("status"));
            out.add(a);
        }
        return out;
    }

    @Data
    public static class ApplicationDTO {
        private int applyId;
        private String startCity;
        private String endCity;
        private String title;
        private Timestamp date;
        private int applyCount;
        private String status;
    }

}

