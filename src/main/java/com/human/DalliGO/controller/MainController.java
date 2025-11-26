package com.human.DalliGO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.human.DalliGO.vo.UserVO;

import jakarta.servlet.http.HttpSession;



@Controller
public class MainController {
	 @GetMapping("/")
	    public String getMain(HttpSession session, Model model) {
	        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
	        model.addAttribute("loginUser", loginUser);
	        return "main";
	    }
	
}
