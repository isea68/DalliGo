package com.human.dalligo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.dalligo.service.JYListService;
import com.human.dalligo.vo.JYListVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class JYListController {
	
	private final JYListService listservice;

	@GetMapping(value="/list", produces="text/html")
	public String getList(@RequestParam(value = "category", required = false) String category,
						  @RequestParam(value = "search", required = false) String search, 
						  HttpServletRequest request, 
						  HttpSession session,
						  Model model) {
		// 1. 로그인 체크
		Object loginUser = session.getAttribute("loginUser");
		if(loginUser == null) {
			return "redirect:/login";
		}
		
		// 전체 게시글 조회
		List<JYListVO> list = listservice.getListAll(category, search);
		model.addAttribute("list", list);
		model.addAttribute("category", category);
		model.addAttribute("search", search);
	
		// Ajax 요청인지 판단
		boolean Ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		
		if(Ajax) {
			return "/community/list :: postItems"; // fragment
		}
		
		return "/community/list"; // 최초 로딩용 전체 페이지
	}
	
	
	@PostMapping("/search")
	public String getResultBySearch(@RequestParam("search") String search, RedirectAttributes redirect) {
		redirect.addAttribute("search", search); // URL 파라미터로 전달
		
		return "redirect:/community/list";
	}
	
	
}

