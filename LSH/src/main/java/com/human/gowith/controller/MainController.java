package com.human.gowith.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

@Controller
public class MainController {
	@Value("${KAKAO_MAP_KEY}")
    private String kakaoKey;
	
	@GetMapping("/")
	public String getMethodName() {
		return "index";
	}
	
	@GetMapping("/kakao")
	public String kakao(Model model) {
		//System.out.println("🔥 Kakao Key from Spring = " + kakaoKey);
		model.addAttribute("kakaoKey", kakaoKey);
		return "kakaoMaps";
	}
	
	@GetMapping("/list")
	public String showList() {
	    return "list";  // 그냥 list.html 보여주기만 하면 끝!
	}
	
	@GetMapping("/api/kakao-key")
	@ResponseBody
	public Map<String, String> getKakaoKey() {
	    Map<String, String> response = new HashMap<>();
	    response.put("key", kakaoKey);  // @Value로 주입된 그 키
	    return response;
	}
}
