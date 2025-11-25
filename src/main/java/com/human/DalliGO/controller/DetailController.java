package com.human.DalliGO.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.human.DalliGO.service.DetailService;
import com.human.DalliGO.vo.DetailVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DetailController {
	private final DetailService detailService;

	@GetMapping("/detail")
	public String getDetail(Model model) {
		List<DetailVO> events = detailService.selectAll();
		model.addAttribute("events", events);
		return "detail";
	}
	
	
	
	
	
	
	
}
