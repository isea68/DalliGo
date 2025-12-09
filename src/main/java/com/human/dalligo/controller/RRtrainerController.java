package com.human.dalligo.controller;


import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	      return "/training/AcademyBoard";	  
	      }	  
	  
	  
	  @PostMapping("/trainerRegister")
	  public String registerTrainer(@ModelAttribute RRtrainerVO trainervo,
			  @RequestParam("photoFile") MultipartFile photoFile
			  ) throws IOException {
		  if(!photoFile.isEmpty()) {
			  
		  //1. 파일 저장 경로

		  String oriname = photoFile.getOriginalFilename();	  
		  
		  // S3버킷으로 업로드
		  String s3Url = trainerservice.s3upload(photoFile);
		  
		  
		  //3. DB에 저장할 경로 설정
		  trainervo.setPhotoOri(oriname) ;
		  trainervo.setPhotoNew(s3Url);
		  }
		  
		  //4. 서비스/DAO 호출해서 DB저장	  
		  trainerservice.insert(trainervo);	  
		  return "redirect:/academy";	  
		
	  } 
	  
	  
	  
	  @PostMapping("/rlogin")
	  public String postlogin(@ModelAttribute RRtrainerVO trainervo , HttpSession session) {  
		  
		  RRtrainerVO loginvo = trainerservice.selectForLogin(trainervo);
		  if(loginvo!=null) { //로그인성공-->세션에 id(pk) 저장 
			 session.setAttribute("trainerPk", loginvo.getId());
			// session.setAttribute("trainerId", loginvo.getTrainerId());
			  return "redirect:/course";
		  }else {
			  return "redirect:/rlogin?error";
		  }     
	  }

}
