package com.human.dalligo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.dalligo.service.JYListService;
import com.human.dalligo.vo.JYListVO;
import com.human.dalligo.vo.JYPageVO;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class JYListController {
	
	private final JYListService listservice;

	@GetMapping(value="/list", produces="text/html")
	public String getList(@RequestParam(value = "category", required=false) String category,
						  @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		
		int pageSize = 10; // 한 번에 보여줄 게시글 수
		
		JYPageVO pagevo = listservice.getPage(category, page, pageSize);	
		model.addAttribute("pagevo", pagevo);
		model.addAttribute("category", category);
		
		boolean hasCategory = (category != null && !category.isEmpty());
		
		// page > 1 이면 AJAX 요청 -> fragment 반환
		if (page > 1 || hasCategory) {
			return "/community/list :: postItems"; // fragment
		}
		
		return "/community/list"; // 전체 페이지
	}
	
	
	@PostMapping("/search")
	public String getResultBySearch(@RequestParam("search") String search, Model model) {
		List<JYListVO> list = listservice.getResultBySearch(search);
		
		model.addAttribute("list", list);
		
		return "/community/list";
	}
	
	
}
