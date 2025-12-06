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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.dalligo.service.LshEventService;
import com.human.dalligo.service.LshTripService;
import com.human.dalligo.service.LshTripService.ApplicationDTO;
import com.human.dalligo.vo.LshCityVO;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LshTripController {

    private final LshTripService tripService;
    private final LshEventService eventService;

    /** 특정 이벤트의 Trip 상세 */
    @GetMapping("/events/{id}/trips") 
    public String tripDetailForEvent(@PathVariable("id") int eventId, Model model) {

        LshEventVO event = eventService.getEvent(eventId);

        LshTripVO trip = tripService.getTripByEvent(eventId);

        // ⭐ trip 없으면 자동 생성
        if (trip == null) {
            trip = tripService.createTripFromEvent(event, "leesh");
        }

        Date startDate = Date.from(event.getStartDate().atZone(ZoneId.systemDefault()).toInstant());
        Date endDate   = Date.from(event.getEndDate().atZone(ZoneId.systemDefault()).toInstant());

        model.addAttribute("event", event);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("trip", trip);
        //model.addAttribute("tripDetail", trip);
        model.addAttribute("minPeople", 25); // 상수


        // 출발지 / 도착지
        String startCity = "서울";
        String endCity = trip.getEndCity();

        model.addAttribute("startCity", startCity);
        model.addAttribute("endCity", endCity);

        // Kakao Map용 DB 도시 정보
        LshCityVO startCityInfo = tripService.getCity(startCity);
        LshCityVO endCityInfo = tripService.getCity(endCity);
        model.addAttribute("startCityInfo", startCityInfo);
        model.addAttribute("endCityInfo", endCityInfo);

        return "trip/detail";
    }

    /** 거리 계산 후 cost + status AJAX 반환 */
    @PostMapping("/trips/{id}/distance")
    @ResponseBody
    public Map<String, Object> updateDistanceAndCompute(
            @PathVariable("id") int eventId,
            @RequestParam("id") BigDecimal distance) {

        LshTripVO trip = tripService.updateOrCreateTripWithDistance(eventId, "leesh", distance);
        String status = tripService.computeStatus(trip, eventService.getEvent(eventId));

        Map<String, Object> m = new HashMap<>();
        m.put("tripId", trip.getTripId());
        m.put("cost", trip.getCost());
        m.put("status", status);
        return m;
    }

    /** Apply 클릭 */
	@PostMapping("/trips/{tripId}/apply")
	public String applyToTrip(@PathVariable("tripId") int tripId, RedirectAttributes ra) {

		String userId = "leesh"; // 현재 하드코딩

		tripService.applyToTrip(tripId, userId);

		ra.addFlashAttribute("msg", "신청이 완료되었습니다.");
		return "redirect:/trips/" + tripId;
	}

    /** 신청 목록 */
    @GetMapping("/applications")
    public String applications(Model model) {

        List<ApplicationDTO> apps = tripService.getAllApplicationsWithEventInfo();
        model.addAttribute("apps", apps);

        return "trip/list";
    }

    /** 이벤트 상세 → Trip 상세 */
    @GetMapping("/events/{id}/trip-detail")
    public String eventTripDetail(
            @PathVariable("id") int id,
            Model model) {

        String userId = "leesh";  // 임시 고정값

        LshEventVO event = eventService.getEvent(id);
        LshTripVO trip = tripService.getTripByUserAndEvent(userId, id);

        model.addAttribute("event", event);
        model.addAttribute("tripDetail", trip);

        return "trip/detail";
    }


}

