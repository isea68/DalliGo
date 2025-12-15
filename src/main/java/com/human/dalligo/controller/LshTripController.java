package com.human.dalligo.controller;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.dalligo.service.LshDistanceService;
import com.human.dalligo.service.LshEventService;
import com.human.dalligo.service.LshTripService;
import com.human.dalligo.vo.JSUserVO;
import com.human.dalligo.vo.LshApplyListVO;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LshTripController {

    private final LshTripService tripService;
    private final LshEventService eventService;
    private final LshDistanceService distanceService;


    /** 이벤트의 Trip 상세 조회 
     * @param startCity */
    @GetMapping("/events/{id}/trips")
    public String tripDetailForEvent(
            @PathVariable("id") int eventId,
            @SessionAttribute("loginUser") JSUserVO loginUser,
            Model model, Object startCity) {
    	
    	// 🔐 비회원 접근 차단 (Whitelabel 방지)
        if (loginUser == null) {
            return "redirect:/events";
        }

        String userAddr = loginUser.getAddress();
        System.out.printf("userAddr = "+userAddr);
        model.addAttribute("loginUser", loginUser);

        LshEventVO event = eventService.getEvent(eventId);
        LshTripVO trip = tripService.getTripByEvent(eventId);

        // Trip 없으면 자동 생성
        if (trip == null) {
            trip = tripService.createTripFromEvent(event, loginUser.getUserId());
        }

        model.addAttribute("event", event);
        model.addAttribute("startDate",
                Date.from(event.getStartDate().atZone(ZoneId.systemDefault()).toInstant()));
        model.addAttribute("endDate",
                Date.from(event.getEndDate().atZone(ZoneId.systemDefault()).toInstant()));
        model.addAttribute("trip", trip);
        model.addAttribute("minPeople", 25);

        // 출발/도착 도시 계산
        model.addAttribute("startCity", tripService.extractCity(userAddr));
        model.addAttribute("endCity", tripService.extractCity(event.getLocation()));
        
		// ✅ cities 테이블의 주소 조회 (이미 있는 서비스 가정)
        String cityName=tripService.extractCity(event.getLocation());
        System.out.println("cityName = "+cityName);
        model.addAttribute("startCityAddr", userAddr);
        model.addAttribute("endCityAddr", tripService.getCityAddress(cityName));

        return "trip/detail";
    }


    // 거리 업데이트 : trip객체에 distance를 같이 넘겨야 함
    @GetMapping("/trips/{id}")
    public String showTripDetail(@PathVariable("id") int tripId, Model model) {

        LshTripVO trip = tripService.getTripById(tripId);
        model.addAttribute("trip", trip);

        // 1) 주소에서 도시 추출
        String startCity = trip.getStartCity(); // 또는 trip.getStart_address()
        String endCity   = trip.getEndCity();   // 또는 trip.getEvent_location()

        startCity = tripService.extractCity(startCity);
        endCity   = tripService.extractCity(endCity);

        // 2) 콘솔 확인용 로그
        System.out.println("=== 거리 계산 ===");
        System.out.println("출발지: " + startCity);
        System.out.println("도착지: " + endCity);

        // 3) DB에서 거리 조회
        Integer distance = distanceService.getDistance(startCity, endCity);

        System.out.println("거리(km): " + distance);

        // 4) 화면 전달
        model.addAttribute("distance", distance);

        return "trip/detail";
    }


    /** 이벤트 신청 */
    @PostMapping("/apply")
    @ResponseBody
    public Map<String, Object> apply(
            @SessionAttribute("loginUser") JSUserVO loginUser,
            @RequestBody Map<String, Object> req) {

        Map<String, Object> res = new HashMap<>();

        if (loginUser == null) {
            res.put("ok", false);
            res.put("message", "로그인이 필요합니다.");
            res.put("currentPeople", 0);
            return res;
        }

        String userId = loginUser.getUserId();

        // event_id 또는 eventId 둘 다 허용
        Object e1 = req.get("event_id");
        Object e2 = req.get("eventId");
        Integer eventId = null;
        if (e1 instanceof Number) eventId = ((Number)e1).intValue();
        else if (e2 instanceof Number) eventId = ((Number)e2).intValue();
        else if (e1 instanceof String) eventId = Integer.valueOf((String)e1);
        else if (e2 instanceof String) eventId = Integer.valueOf((String)e2);

        if (eventId == null) {
            res.put("ok", false);
            res.put("message", "event_id 가 전달되지 않았습니다.");
            res.put("currentPeople", 0);
            return res;
        }

        int applied = tripService.applyToEvent(eventId, userId);

        res.put("ok", applied);
        res.put("message", applied>0 ? "신청이 완료되었습니다." : "이미 신청하셨습니다.");
        res.put("currentPeople", tripService.getCurrentPeople(eventId)); // count(*) 기반
        return res;
    }

    @PostMapping("/trip/cancel")
    @ResponseBody
    public Map<String, Object> cancelTrip(
            @SessionAttribute("loginUser") JSUserVO loginUser,
            @RequestBody Map<String, Object> payload) {

        Map<String, Object> result = new HashMap<>();

        if (loginUser == null) {
            result.put("ok", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        Object ev = payload.get("eventId");
        Integer eventId = null;
        if (ev instanceof Number) eventId = ((Number)ev).intValue();
        else if (ev instanceof String) eventId = Integer.valueOf((String) ev);

        if (eventId == null) {
            result.put("ok", false);
            result.put("message", "eventId 누락");
            return result;
        }

        String userId = loginUser.getUserId();
        boolean ok = tripService.cancelApplication(eventId, userId);

        result.put("ok", ok);
        result.put("message", ok ? "신청이 취소되었습니다." : "취소 실패 또는 이미 취소됨");
        result.put("currentPeople", tripService.getCurrentPeople(eventId)); // 최신 카운트 반환
        return result;
    }


    /** 신청 목록 조회 */
    @GetMapping("/trip/list")
    public String applyList(Model model) {
        List<LshApplyListVO> applyLists = tripService.getAllApplicationsWithEventInfo();
        model.addAttribute("applyLists", applyLists);
        return "trip/list";
    }

    /** (선택) 이벤트 상세 → Trip 상세 이동 */
    @GetMapping("/events/{id}/trip-detail")
    public String eventTripDetail(
            @PathVariable("id") int id,
            @SessionAttribute("loginUser") JSUserVO loginUser,
            Model model) {

        LshEventVO event = eventService.getEvent(id);
        LshTripVO trip = tripService.getTripByUserAndEvent(loginUser.getUserId(), id);

        model.addAttribute("event", event);
        model.addAttribute("tripDetail", trip);

        return "trip/detail";
    }
}