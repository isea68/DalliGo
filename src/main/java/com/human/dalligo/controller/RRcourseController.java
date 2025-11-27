package com.human.dalligo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.dalligo.service.RRcourseService;
import com.human.dalligo.vo.RRcourseVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Controller
public class RRcourseController {
	
 private final  RRcourseService courseservice;
	
	
	
  @GetMapping("/academy")
  public String getTrain(Model model) {
	  
	  List<RRcourseVO> course = courseservice.selectList();
	  model.addAttribute("course", course);	  
      return "/training/AcademyBoard";
  }  

  
  @GetMapping("/course")
  public String getcourse() {
      return "/training/course";
  } 
  
  @PostMapping("/course")
  public String postcourse(@ModelAttribute RRcourseVO coursevo, 
		  					HttpSession session,
		  					@RequestParam("prPhotoFile") MultipartFile prPhotoFile
		  ) throws IOException {
	  	  
	  //---트레이너 ID 점검부분-----//
	  // 세션에서 로그인한 trainerId 저장한거 가져오기 (getAttribute)	 
	  Integer trId =(Integer)session.getAttribute("trainerPk");	  
	  System.out.println("trId:"+trId);
	  
	  if(trId==null) {
		  return "redirect:/login";
	  }
	  
	  
	  //VO에다 trainerId 셋팅
	  coursevo.setTrainerId(trId); 
	  
	  //---트레이너 ID 점검부분-----//	
	 
	  if(!prPhotoFile.isEmpty()) {
	  String uploadDir = "C:/upload/";
	  String filename = System.currentTimeMillis()+"_"+prPhotoFile.getOriginalFilename();
	  File saveFile = new File(uploadDir+filename);
	  prPhotoFile.transferTo(saveFile);	  
	  coursevo.setPrPhotoUrl("/upload/"+filename);   
	  }
	  
	  //DB에 저장
	  courseservice.insert(coursevo);      
	  return "redirect:/";
  }      
  

  
}
