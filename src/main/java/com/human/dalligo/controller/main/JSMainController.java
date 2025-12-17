package com.human.dalligo.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.human.dalligo.vo.user.JSUserVO;

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
