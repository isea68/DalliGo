package com.human.dalligo.controller.academy;


import java.io.IOException;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.dalligo.service.academy.CourseService;
import com.human.dalligo.service.academy.TrainerService;
import com.human.dalligo.vo.JSUserVO;
import com.human.dalligo.vo.academy.CourseVO;
import com.human.dalligo.vo.academy.TrainerVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
//@RequestMapping(/academy)
public class CourseController {
	
	
 private final CourseService courseservice;
 private final TrainerService trainerservice;
	
	
 	//강좌의 main화면---------------------------------------------
  
  @GetMapping("/academy")
  public String getTrain(Model model) {	  
	  List<CourseVO> courses = courseservice.selectList();

	  model.addAttribute("courses", courses);	  
      return "/training/AcademyBoard";
  }  


   //강좌의 상세페이지---------------------------------------------
  @GetMapping("/training/AcademyDetail")    
  public String gettrainingDetail(@RequestParam("id") int id,
		  							HttpSession session,
		  							Model model) {	    
	  
	  //1.강좌 정보갖고 오기
	  CourseVO courseId = courseservice.selectById(id) ;
	  
	  //2.강좌정보에서 TrainerId 조회
	  int trainerId =courseId.getTrainerId();
	  
	  //3.트레이너 정보 조회	  
	  TrainerVO trainervo =trainerservice.select(trainerId);    
	  if (trainervo==null) {			  
		  //개발용 sysout 대신 로그 사용
		  log.warn("조회 트레이너 없음:id={}",trainerId );	
	  }else {		  
		  log.info("조회강좌:{}", trainervo);	
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
	  
	  JSUserVO loginUser = (JSUserVO)session.getAttribute("loginUser");
	  if(loginUser==null) {
		  model.addAttribute("isGuest",true);// 비회원임
		  model.addAttribute("user", null); //예외처리
	  }else {
		  model.addAttribute("isGuest", false);
		  model.addAttribute("user", loginUser); 
	  }
	  
	 //.addAttribute("user", loginUser);	  
	  model.addAttribute("historyList", historyList);
	  model.addAttribute("tvo", trainervo);	  
	  model.addAttribute("monStr", monStr);
	  model.addAttribute("dayStr", dayStr);
	  model.addAttribute("timeStr", timeStr);
	  model.addAttribute("cId", courseId); 	  
	  
	  return "/training/AcademyDetail";
  } 
  
  
  // 강좌등록 화면---------------------------------------------
  @GetMapping("/course")  
  public String getcourse() {
      return "/training/course";
  } 
  

  	//post--> 강좌등록 데이터  ---------------------------------------------
  @PostMapping("/course") 
  public String postcourse(@ModelAttribute CourseVO coursevo, 
		  					HttpSession session,
		  					@RequestParam("prphotoFile") MultipartFile prphotoFile
		  ) throws IOException {
	  	  
	  //---트레이너 ID 점검부분-----
	  // CourseVO에 trainerId부분은 트레이너의 PK를 갖고와야한다.
	  // 그래서 세션에서 trainerPK라 명명하고 갖고와서 CourserVO에 저장해준다
	  TrainerVO trCall =(TrainerVO)session.getAttribute("trainerlogin");
	  
	  if(trCall==null) {
		  //로그인 안돼음
	  }
	  
	  Integer trId = trCall.getId();
	  
	 // Integer trId =(Integer)session.getAttribute("trainerPk");	  
	 // System.out.println("trId:"+trId);	  
	  
	  if(trId==null) {
		  return "redirect:/rlogin";	  }	  
	  
	  //VO에다 트레이너ID가 아닌 트레이너VO의 PK값을 갖고 와서 저장
	  coursevo.setTrainerId(trId); 
	  
	  //---트레이너 ID 점검부분-----		 
	  if(!prphotoFile.isEmpty()) {

	  String oriName = prphotoFile.getOriginalFilename();

	  // S3버킷으로 업로드
	  String s3Url = trainerservice.s3upload(prphotoFile);
	  
	  //
	  coursevo.setPrphotoOri(oriName);
	  coursevo.setPrphotoNew(s3Url);
	  
	  }
	  // 삭제용 토근 생성
	  coursevo.setDeleteToken(UUID.randomUUID().toString());
	  
	  //DB에 저장
	  courseservice.insert(coursevo);      
	  return "redirect:/academy";
  }     
     
  
    //강좌 삭제   : 강좌번호와 토큰 넘기기기----------------------------------------------
   @GetMapping("/trainer/ad/del/{id}")
	public String deleteAd(@PathVariable("id") Integer id, 
							@RequestParam("token") String token,
							RedirectAttributes rttr) {		
		try {
			courseservice.deleteByToken(id, token);
			
		} catch (RuntimeException e) {
			rttr.addFlashAttribute("error", "삭제 실패: 잘못된 요청입니다.");
			return "redirect:/academy"; //
		}
		rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");
		return "redirect:/academy"; // 삭제 후 목록으로 이동
	}
   
   
	  //강사 마이페이지 만들기---------------------------------------------
	  @GetMapping("/trainerPage")
	  public String trainerpage(
			  	@SessionAttribute(name="trainerlogin", required=false) TrainerVO trCall
			  	, Model model) {
		  
		  //로그인한 강사ID 가져오기
		  //Integer trainerId =(Integer)session.getAttribute("trainerId");
		    Integer trainerPK = trCall.getId();
		    System.out.println("trainerPK="+trainerPK );
		  
		  if(trainerPK==null) {
			  return "redirect:/academy?needTrlog=Notrainer";
		  }
		  //해당 강사가 등록한 모든 강좌 가져오기
		  List<CourseVO> myCourseList = courseservice.findByTrainerId(trainerPK);
		   
	//	  System.out.println("조회된 강좌수:"+myCourseList);
	//	  for(CourseVO vo :myCourseList) {
	//		  System.out.println("강좌:"+vo);
	//	  }
		  
		  model.addAttribute("myCourseList", myCourseList);
	  	 return  "training/trainerPage";
	  }
   

}
