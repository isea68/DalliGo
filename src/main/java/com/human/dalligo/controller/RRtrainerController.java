package com.human.dalligo.controller;


import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.dalligo.service.RRtrainerService;
import com.human.dalligo.vo.RRtrainerVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/training")

public class RRtrainerController {
	
	 private final  RRtrainerService trainerservice;

	 //강사회원가입 화면---------------------------------------------
	  @GetMapping("/trainerRegister")
	  public String registerview() {
	      return "/training/trainerRegister";
	  }
	  
	  
	  //강사로그인화면---------------------------------------------
	  @GetMapping("/trainerCall")
	  public String trainerCallpage() {
	  	return "/training/trainerCall";
	  }	  
	  
	  
	  //강사회원가입---------------------------------------------
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
	  
	  
	  // 강사로그인-->목적지가 2군데 (강좌등록시/강사페이지볼때) 판별하는 기능
	  @PostMapping("/trlogin")
	  public String postlogin(@ModelAttribute RRtrainerVO trainervo,
	                          @RequestParam(name="dest",required = false) String dest,
	                          HttpSession session) {
	      RRtrainerVO loginvo = trainerservice.selectForLogin(trainervo);
	      if (loginvo != null) {
	    	  
	        //  session.setAttribute("trainerPk", loginvo.getId());	
	    	   session.setAttribute("trainerlogin", loginvo);

	          // 목적지 분기
	          if ("trainerPage".equals(dest)) {
	              return "redirect:/trainerPage";
	          } else {
	              return "redirect:/course";
	          }
	      } else {
	    	  //실패시 다시 강사로그인화면+모달오픈+에러메시지
	          return "redirect:/training/trainerCall?error=trainerCallFail&dest="+dest;
	      }
	  }

}
