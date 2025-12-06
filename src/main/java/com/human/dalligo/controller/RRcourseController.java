package com.human.dalligo.controller;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.dalligo.service.RRcourseService;
import com.human.dalligo.service.RRtrainerService;
import com.human.dalligo.vo.RRcourseVO;
import com.human.dalligo.vo.RRtrainerVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Controller
public class RRcourseController {
	
 private final  RRcourseService courseservice;
 private final RRtrainerService trainerservice;
	
	
	
  @GetMapping("/academy")
  public String getTrain(Model model) {	  
	  List<RRcourseVO> courses = courseservice.selectList();
	  model.addAttribute("courses", courses);	  
      return "/training/AcademyBoard";
  }  


  
  @GetMapping("/training/AcademyDetail")    
  public String gettrainingDetail(@RequestParam("id") int id,
		  							HttpSession session,
		  							Model model) {	    
	  
	  //1.강좌 정보갖고 오기
	  RRcourseVO courseId = courseservice.selectById(id) ;
	  
	  //2.강좌정보에서 TrainerId 조회
	  int trainerId =courseId.getTrainerId();
	  
	  //3.트레이너 정보 조회	  
	  RRtrainerVO trainervo =trainerservice.select(trainerId);    
	  if (trainervo==null) {	
		  System.out.println("조회 트레이너 없음");
	  }else {
		  System.out.println("조회강좌:"+trainervo);
	  }
	  
	  //4.트레이너의 히스토리 데이터를 '|'기준으로 분리해서 정렬하기 위한 작업
	  String[] historyList = trainervo.getHistory()!=null? trainervo.getHistory().split("\\|") :new String[0];
			  
	  
	  // LocalDateTime으로 반환(DB에서 가져온 값이 javj.util.Date면 toInstant 후 변환)
	  LocalDateTime dt =courseId.getStartDate();
	  
	  //월
	  String monStr =dt.getMonthValue()+"월";
	  
	  //요일
	  String[] weekdays = {"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};
	  String dayStr = weekdays[dt.getDayOfWeek().getValue()-1];
	  
	  //시간 (오전/오후 포함)
	  DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("a h시",Locale.KOREAN);
	  String timeStr =dt.format(timeFormat);
	  
	  model.addAttribute("historyList", historyList);
	  model.addAttribute("tvo", trainervo);	  
	  model.addAttribute("monStr", monStr);
	  model.addAttribute("dayStr", dayStr);
	  model.addAttribute("timeStr", timeStr);
	  model.addAttribute("cId", courseId);
	  return "/training/AcademyDetail";
  } 
  
  
  @GetMapping("/course")  // Get--> 주로 화면열기/폼 보여주기
  public String getcourse() {
      return "/training/course";
  } 
  
  
  @PostMapping("/course") //post--> 폼전송/데이터 저장
  public String postcourse(@ModelAttribute RRcourseVO coursevo, 
		  					HttpSession session,
		  					@RequestParam("prPhotoFile") MultipartFile prPhotoFile
		  ) throws IOException {
	  	  
	  //---트레이너 ID 점검부분-----//
	  // 세션에서 로그인한 trainerId 저장한거 가져오기 (getAttribute)	 
	  Integer trId =(Integer)session.getAttribute("trainerPk");	  
	 // System.out.println("trId:"+trId);
	  
	  if(trId==null) {
		  return "redirect:/rlogin";	  }	  
	  
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
	  return "redirect:/academy";
  }      
  

  
}
