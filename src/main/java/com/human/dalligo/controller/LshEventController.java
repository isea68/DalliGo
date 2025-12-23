package com.human.dalligo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.human.dalligo.service.LshEventService;
import com.human.dalligo.service.LshTripService;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshPageVO;
import com.human.dalligo.vo.LshTripVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class LshEventController {

	private final LshEventService eventService;
	private final LshTripService tripService;

    // 전체 이벤트 목록 페이지
//	@GetMapping({"/events"})
//	public String rootToEvents(Model model, HttpSession session) {
//		//List<LshEventVO> list = eventService.getAllEvents();
//		model.addAttribute("events", eventService.getAllEvents());
//		model.addAttribute("isLogin", session.getAttribute("loginUser") != null);
//		return "event/list"; // event/list.html
//	}

    // 이벤트 상세 페이지
	@GetMapping("/{id}")
	public String eventDetail(@PathVariable int id, Model model) {

		LshEventVO ev = eventService.getEvent(id);
	    if (ev == null) return "error";
	    model.addAttribute("event", ev);
	    // trips for the event (may be empty)
	    List<LshTripVO> trips = tripService.getTripsByEvent(id);
	    model.addAttribute("trips", trips);
	    return "trip/detail";
	}
	
	@GetMapping({ "", "/" })
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

}

