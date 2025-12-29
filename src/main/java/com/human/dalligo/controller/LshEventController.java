package com.human.dalligo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.dalligo.service.LshEventService;
import com.human.dalligo.service.LshTripService;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshPageVO;
import com.human.dalligo.vo.LshTripVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class LshEventController {

	private final LshEventService eventService;
	private final LshTripService tripService;

    // 이벤트 상세 페이지
	@GetMapping("/events/{id}")
	public String eventDetail(@PathVariable("id") int id, Model model) {

		LshEventVO ev = eventService.getEvent(id);
	    if (ev == null) return "error";
	    model.addAttribute("event", ev);
	    // trips for the event (may be empty)
	    List<LshTripVO> trips = tripService.getTripsByEvent(id);
	    model.addAttribute("trips", trips);
	    return "trip/detail";
	}
	
	@GetMapping({"/events", "/events/"})
	public String eventList(
	        @RequestParam(value = "page", required = false, defaultValue = "1") Integer  page,
	        Model model,
	        HttpSession session) {

	    int size = 10;

	    int totalCount = eventService.getTotalEventCount();
	    LshPageVO pageVO = new LshPageVO(page, size, totalCount);

	    List<LshEventVO> eventList =
	            eventService.getEventList(pageVO.getOffset(), size);

	    model.addAttribute("eventList", eventList);
	    model.addAttribute("pageVO", pageVO);
	    model.addAttribute("isLogin", session.getAttribute("loginUser") != null);

	    return "event/list";
	}

	// 검색용 비동기 방식 (fetch) 컨트롤러 추가
	@ResponseBody
	@GetMapping("/events/search")
	public Map<String, Object> getEvents(
	        @RequestParam(name = "page", defaultValue = "1") int page,
	        @RequestParam(name = "keyword", required = false) String keyword) {

	    int size = 10;

	    int totalCount = eventService.getTotalEventCount(keyword);
	    LshPageVO pageVO = new LshPageVO(page, size, totalCount);

	    List<LshEventVO> list =
	            eventService.getEventListWithSearch(
	                    pageVO.getOffset(), size, keyword);

	    // ★ 프론트에서 바로 쓰기 좋게 명확히 반환
	    Map<String, Object> result = new HashMap<>();
	    result.put("list", list);
	    result.put("totalCount", totalCount);   // 필요 시 확장 가능

	    return result;
	}


}

