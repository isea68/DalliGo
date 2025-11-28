package com.human.dalligo.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.dalligo.service.RRtrainerService;

import com.human.dalligo.vo.RRtrainerVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor

public class RRtrainerController {
	
	 private final  RRtrainerService trainerservice;
	
	  @GetMapping("/trainerRegister")
	  public String registerview() {
	      return "/training/trainerRegister";
	  }
	  
	  @GetMapping("/rlogin")
	  public String getlogin() {
	      return "/training/login";	  }
	  
	  
	  
	  @PostMapping("/trainerRegister")
	  public String registerTrainer(@ModelAttribute RRtrainerVO trainervo,
			  @RequestParam("photoFile") MultipartFile photoFile
			  ) throws IOException {
		  if(!photoFile.isEmpty()) {
			  
		  //1. 파일 저장 경로
		  String uploadDir = "C:/upload/"; //서버로컬 디렉토리 
		  String filename = System.currentTimeMillis()+"_"+photoFile.getOriginalFilename();
		  File saveFile = new File(uploadDir+filename);
		  
		  //2. 파일 저장
		  photoFile.transferTo(saveFile);
		  
		  //3. DB에 저장할 경로 설정
		  trainervo.setPhotoUrl("/upload/"+filename);
		  }
		  
		  //4. 서비스/DAO 호출해서 DB저장	  
		  trainerservice.insert(trainervo);	  
		  return "redirect:/rlogin";
	  } 
	  
	  
	  
	  @PostMapping("/rlogin")
	  public String postlogin(@ModelAttribute RRtrainerVO trainervo , HttpSession session) {  
		  
		  RRtrainerVO loginvo = trainerservice.selectForLogin(trainervo);
		  if(loginvo!=null) { //로그인성공-->세션에 id(pk) 저장 
			  session.setAttribute("trainerPk", loginvo.getId());
			  return "redirect:/course";
		  }else {
			  return "redirect:/rlogin?error";
		  }     
	  }
	  

}
