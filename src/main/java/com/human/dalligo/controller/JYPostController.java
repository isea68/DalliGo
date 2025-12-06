package com.human.dalligo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.dalligo.service.JYPostService;
import com.human.dalligo.vo.JSUserVO;
import com.human.dalligo.vo.JYFileVO;
import com.human.dalligo.vo.JYPostVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class JYPostController {
	
	private final JYPostService postservice;
	
	
	@GetMapping("/post")
	public String getList(@RequestParam(value = "category", required = false, defaultValue="") String category,
						  @RequestParam(value = "search", required = false, defaultValue="") String search,
						  HttpSession session,
						  RedirectAttributes redirect) {
		
        // 로그인 체크
        Object loginUser = session.getAttribute("loginUser");
            if(loginUser == null) {
            	// list.html로 redirect하려면 파라미터가 필요
            	redirect.addAttribute("category", category);
            	redirect.addAttribute("search", search);
            	
            	//alert창 띄우기 용 - category가 null일 경우 비회원이 글쓰기 시도하려 함 -> script에서 alert창 띄우기
            	redirect.addFlashAttribute("alertLogin", "1"); // FlashAttribute는 redirect 이후에도 딱 한번! 데이터 전달하기 위해 만든 기능
            	return "redirect:/community/list";
            }
		
		return "/community/post";
	}
	
	@PostMapping("/post/upload")
	public String getPostUpload(@ModelAttribute JYPostVO postvo,
								@RequestParam(value="files", required=false) List<MultipartFile> files,
								HttpSession session) throws IOException {
		
		// 세션에 저장된 userFk를 postvo에 세팅
		JSUserVO loginUser = (JSUserVO) session.getAttribute("loginUser");
		postvo.setUserFk(loginUser.getId());
	
		// 1. 파일 경로 저장
		List<String> fileList = new ArrayList<>();
		
		// 3. DB에 저장
		postservice.insert(postvo, files);
		
		return "redirect:/community/list";
	}
	
}
