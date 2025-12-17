package com.human.dalligo.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.dalligo.service.community.JYCommentService;
import com.human.dalligo.vo.community.JYCommentVO;
import com.human.dalligo.vo.user.JSUserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class JYCommentController {
	
	private final JYCommentService commentservice;
	
	// 댓글 등록
	@PostMapping("/uploadComment")
	@ResponseBody
	public JYCommentVO insertComment(@RequestBody JYCommentVO commentvo, HttpSession session) {
		JSUserVO loginUser = (JSUserVO) session.getAttribute("loginUser");
							//getAtrribute반환 타입이 Object여서 (UserVO)로 강제 형변환(casting)
		
		// DB user.id를 FK로 설정
		commentvo.setUserFk(loginUser.getId());
		
		// service에서 DB insert + select 처리
		JYCommentVO insertedComment = commentservice.insertComment(commentvo);
		return insertedComment; 
	}
	
	// 댓글 수정
	@PutMapping("/modComment/{commentId}")
	@ResponseBody
	public JYCommentVO updateComment(@PathVariable("commentId") int commentId, 
									 @RequestBody JYCommentVO commentvo) {
		
		commentvo.setId(commentId); // PathVariable로 받은 id 설정
		JYCommentVO updatedComment = commentservice.updateComment(commentvo);
		
		return updatedComment;
	}
	
	// 댓글 삭제
	@DeleteMapping("/delComment/{commentId}")
	@ResponseBody
	public String deleteComment(@PathVariable("commentId") int commentId) {
		commentservice.deleteComment(commentId);
		
		return "success"; // 성공 시 클라이언트에서 처리
	}

}
