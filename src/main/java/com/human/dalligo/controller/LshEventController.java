package com.human.dalligo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.human.dalligo.service.LshEventService;
import com.human.dalligo.service.LshTripService;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripVO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequiredArgsConstructor
public class LshEventController {

	private final LshEventService eventService;
	private final LshTripService tripService;

// 전체 이벤트 목록 페이지
	@GetMapping({"/events"})
	public String rootToEvents(Model model) {
		//List<LshEventVO> list = eventService.getAllEvents();
		model.addAttribute("events", eventService.getAllEvents());
		return "event/list"; // event/list.html
	}

// 이벤트 상세 페이지
	@GetMapping("/events/{id}")
	public String eventDetail(@PathVariable int id, Model model) {

		LshEventVO ev = eventService.getEvent(id);
	    if (ev == null) return "error";
	    model.addAttribute("event", ev);
	    // trips for the event (may be empty)
	    List<LshTripVO> trips = tripService.getTripsByEvent(id);
	    model.addAttribute("trips", trips);
	    return "event/detail";
	}
	 

// 이벤트 등록 처리
//	@PostMapping("/events/add")
//	public String addEvent(LshEventVO vo) {
//		eventService.addEvent(vo);
//		return "redirect:/events";
//	}
}

