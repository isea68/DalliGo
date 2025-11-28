package com.human.dalligo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.human.dalligo.service.UserService;
import com.human.dalligo.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignUpController {

	private final UserService userService;

	@GetMapping("/signup")
	public String postSignup() {

		return "signup";
	}

	@PostMapping("/signup")
	public String postSignup(@ModelAttribute("user") UserVO user, Model model) { //
		userService.insert(user);
		return "redirect:/"; // 가입 후 메인(혹은 로그인)으로 리다이렉트
	}

}
