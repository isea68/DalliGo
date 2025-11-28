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
import com.human.dalligo.vo.JYPostVO;

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
								@RequestParam(value="files", required=false)
								List<MultipartFile> files){
		postservice.insert(postvo, files);
		System.out.println(postvo.toString());
		return "redirect:/community/list";
	}
	
}
