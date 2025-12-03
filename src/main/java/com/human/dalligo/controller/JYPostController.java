package com.human.dalligo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.dalligo.service.JYPostService;
import com.human.dalligo.vo.JSUserVO;
import com.human.dalligo.vo.JYPostVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class JYPostController {
	
	private final JYPostService postservice;
	
	
	@GetMapping("/post")
	public String getList() {
		return "/community/post";
	}
	
	@PostMapping("/post/upload")
	public String getPostUpload(@ModelAttribute JYPostVO postvo,
								@RequestParam(value="files", required=false) List<MultipartFile> files,
								HttpSession session){
		
		// 세션에 저장된 userFk를 postvo에 세팅
		JSUserVO loginUser = (JSUserVO) session.getAttribute("loginUser");
		postvo.setUserFk(loginUser.getId());
		
		postservice.insert(postvo, files);
		
		return "redirect:/community/list";
	}
	
}
