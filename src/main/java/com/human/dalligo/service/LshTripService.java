package com.human.dalligo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.human.dalligo.dao.LshEventDAO;
import com.human.dalligo.dao.LshTripDAO;
import com.human.dalligo.vo.LshApplyVO;
import com.human.dalligo.vo.LshCityVO;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripVO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LshTripService {

    private final LshTripDAO tripDAO;
    private final LshEventDAO eventDAO;

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
        trip.setTripDate(event.getStartDate().toLocalDate());
        trip.setTripTime(LocalTime.of(6,0)); // 기본 출발시간 06:00
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
            trip.setTripDate(ev.getStartDate().toLocalDate());
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

    /** 상태 계산 */
    public String computeStatus(LshTripVO trip, LshEventVO event) {

        LocalDateTime now = LocalDateTime.now();
        trip.setTripDate(event.getStartDateOnly());

        // deadline → event 시작 날짜의 00:00 시각
        LocalDateTime deadline = event.getStartDateOnly().atStartOfDay();

        // 최소 출발 인원(고정값)
        final int MIN_PEOPLE = 25;

        // 이벤트 시작일이 지난 경우
        if (!now.isBefore(deadline)) {
            return trip.getCurrentPeople() >= MIN_PEOPLE ? "출발확정" : "취소";
        }

        // 이벤트 시작일 이전 + 최소 인원 충족
        if (trip.getCurrentPeople() >= MIN_PEOPLE) {
            return "출발확정";
        }

        // 기본
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
            a.setDate((LocalDateTime) r.get("start_date"));
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
        private LocalDateTime date;
        private int applyCount;
        private String status;
    }
}

