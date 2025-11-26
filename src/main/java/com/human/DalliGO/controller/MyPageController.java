package com.human.DalliGO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.human.DalliGO.vo.UserVO;

import jakarta.servlet.http.HttpSession;


@Controller
public class MyPageController {

	@GetMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
	    UserVO user = (UserVO) session.getAttribute("loginUser");
	    if(user == null) {
	        return "redirect:/"; // 로그인 안 했으면 메인으로
	    }
	    model.addAttribute("userId", user.getUserId());
	    model.addAttribute("name", user.getName());
	    model.addAttribute("nickName", user.getNickName());
	    model.addAttribute("address", user.getAddress());
	    model.addAttribute("phone", user.getPhone());
	    model.addAttribute("isAdmin", user.getIsAdmin());
	    return "mypage";
	}
  
}
