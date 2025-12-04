package com.human.dalligo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.human.dalligo.vo.JSUserVO;

import jakarta.servlet.http.HttpSession;



@Controller
public class JSMainController {
	 @GetMapping("/")
	    public String getMain(HttpSession session, Model model) {
	        JSUserVO loginUser = (JSUserVO) session.getAttribute("loginUser");
	        model.addAttribute("loginUser", loginUser);
	        return "main";
	    }
	
}
