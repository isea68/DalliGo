package com.human.dalligo.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.dalligo.service.JYDetailService;
import com.human.dalligo.vo.JYDetailVO;
import com.human.dalligo.vo.JYPostVO;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class JYDetailController {
	
	private final JYDetailService detailservice;
	
	private static final String UPLOAD_DIR = "C:/Users/human13/OneDrive/Desktop/DalliGO_KJY/uploads";
	
	// detail.html request 메핑
	@GetMapping("/file/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam("savedName") String savedName,
												 @RequestParam(value = "download", required = false) 
												 Boolean download) throws IOException{
		// 서버 경로 저장
		File file = new File(UPLOAD_DIR + "/" + savedName);
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}
		
		Resource resource = new FileSystemResource(file);
		
		// 파일 MIME 타입
		String contentType = Files.probeContentType(file.toPath());
		if (contentType == null) contentType = "application/octet-stream";
		
		ResponseEntity.BodyBuilder response = ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType));
		
		// download=true이면 attachment로 설정 -> 다운로드
		if (download != null && download) {
			response.header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + URLEncoder.encode(savedName, "UTF-8") + "\"");
		}
		
		return response.body(resource);
	}
	
	
	@GetMapping("/detail/{id}")
	public String getdetail(@PathVariable("id") int id, Model model) {
		JYDetailVO detail = detailservice.detailById(id);	
		model.addAttribute("detail", detail);
		
		return "/community/detail";
	}
	
	
	@GetMapping("/modify/{id}")
	public String updatePost(@PathVariable("id") int id, Model model) {
		JYPostVO detailvo = detailservice.getDetailById(id);
		
		model.addAttribute("detailvo", detailvo);
		return "/community/detailMod";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute JYPostVO postvo) {
		detailservice.update(postvo);
		
		return "redirect:/community/detail/" + postvo.getId();
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		detailservice.delete(id);
		return "redirect:/community/list";
	}
}