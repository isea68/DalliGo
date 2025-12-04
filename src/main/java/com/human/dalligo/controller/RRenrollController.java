package com.human.dalligo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.human.dalligo.vo.JSUserVO;

import jakarta.servlet.http.HttpSession;


@Controller
public class RRenrollController {
	
	@PostMapping("/enrolluser")
	public String postEnroll(HttpSession session,Model model) {
		
		JSUserVO loginUser = (JSUserVO)session.getAttribute("loginUser");
		System.out.println("user:"+loginUser.getName());

		model.addAttribute("user", loginUser);		
		return "/training/AcademyDetail";
	}
	

}
