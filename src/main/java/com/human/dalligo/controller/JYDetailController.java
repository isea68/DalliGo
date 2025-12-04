package com.human.dalligo.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.human.dalligo.service.JYDetailService;
import com.human.dalligo.vo.JSUserVO;
import com.human.dalligo.vo.JYCommentVO;
import com.human.dalligo.vo.JYDetailVO;
import com.human.dalligo.vo.JYPostVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;




@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class JYDetailController {
	
	private final JYDetailService detailservice;
	
	private static final String UPLOAD_DIR = "C:/upload1";
			//"C:/Users/human13/OneDrive/Desktop/DalliGO_KJY/uploads";
											// 로컬 저장소 -> 추후에 수정해야 함!!!! /
	
	// detail.html request 메핑
	@GetMapping("/file/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam("savedName") String savedName,
												 @RequestParam(value = "download", required = false) 
												 Boolean download) throws IOException{
		// 서버 경로 저장
		File file = new File(UPLOAD_DIR + "/" + savedName);
		System.out.println("파일 경로: " + file.getAbsolutePath());
		System.out.println("파일 존재 여부: " + file.exists());
		
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}
		
		Resource resource = new FileSystemResource(file);
		
		// 파일 MIME 타입
		String contentType = Files.probeContentType(file.toPath());
		if (contentType == null) contentType = "application/octet-stream";
		
		ResponseEntity.BodyBuilder response = ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType));
		
		// 다운로드용일 때 attachment, 아니면 inline
		if (download != null && download) {
			response.header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + URLEncoder.encode(savedName, "UTF-8") + "\"");
		} else {
			response.header(HttpHeaders.CONTENT_DISPOSITION,
					"inline; filename=\"" + URLEncoder.encode(savedName, "UTF-8") + "\"");
		}
		
		return response.body(resource);
	}
	
	
	// 단일 게시글 자세히 보기
	@GetMapping("/detail/{id}")
	public String getdetail(@PathVariable("id") int postId,
							@RequestParam(value = "category", required = false, defaultValue="") String category,
							@RequestParam(value = "search", required = false, defaultValue="") String search,
							HttpSession session,
							Model model) {
		
		// 게시글 정보 조회
		JYDetailVO detail = detailservice.detailById(postId);
		List<JYCommentVO> commentList = detailservice.getCommentsById(postId);
		
		model.addAttribute("detail", detail);
		model.addAttribute("commentList", commentList);
		
		// 로그인 사용자 정보 가져오기
		JSUserVO loginUser = (JSUserVO) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		
		// 초기 하트 상태 설정
		boolean isLikedByUser = false;
		String userId = loginUser.getUserId();
		isLikedByUser = detailservice.isLiked(postId, userId);
		model.addAttribute("isLikeByUser", isLikedByUser);
		
		int countLikes = detailservice.countLikes(postId);
		model.addAttribute("countLikes", countLikes);

		
		// 뒤로가기용
		model.addAttribute("category", category);
		model.addAttribute("search", search);
		
		return "/community/detail";
	}
	
	// 좋아요 토글
	@PostMapping("/post/{postId}/like")
	@ResponseBody
	public Map<String, Object> toggleLike(@PathVariable("postId") int postId,
										  @SessionAttribute("loginUser") JSUserVO loginUser,
										  Model model) {
		
		String userId = loginUser.getUserId(); // UserVO에서 userId 가져오기
		boolean isLiked = detailservice.toggleLike(postId, userId);
		int likeCount = detailservice.countLikes(postId);
		
		Map<String, Object> result = new HashMap<>();
		result.put("liked", isLiked);
		result.put("count", likeCount);

		return result;
	}
	
	// 수정할 단일 게시글 조회
	@GetMapping("/update/{id}")
	public String updatePost(@PathVariable("id") int id, Model model) {
		JYPostVO detailvo = detailservice.getDetailById(id);
		
		model.addAttribute("detailvo", detailvo);
		return "/community/detailMod";
	}
	
	// 조회한 단일 게시글 수정 처리
	@PostMapping("/update")
	public String update(@ModelAttribute JYPostVO postvo,
						 @RequestParam(value = "category", required = false) String category,
						 @RequestParam(value = "search", required = false) String search) {
		
		detailservice.update(postvo);
		
		// 수정 후 detail.html로 redirect, list 상태 유지
		String redirectUrl = "/community/detail/" + postvo.getId();
		boolean hasParam = false;
		
		if(category != null) {
			redirectUrl += "?category=" + category;
			hasParam = true; // 이미 ?를 썼다는 표시
		}
		
		if(search != null) {
			// 이미 ?를 썼으면 & 사용, 안 썼으면 ? 사용
			redirectUrl += hasParam ? "&search=" + search : "?search=" + search;
		}
		
		return "redirect:" + redirectUrl;
	}
	
	// 단일 게시글 삭제
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		detailservice.delete(id);
		return "redirect:/community/list";
	}
	
	// 댓글 등록
	@PostMapping("/uploadComment")
	@ResponseBody
	public JYCommentVO insertComment(@RequestBody JYCommentVO commentvo, HttpSession session) {
		JSUserVO loginUser = (JSUserVO) session.getAttribute("loginUser");
							//getAtrribute반환 타입이 Object여서 (UserVO)로 강제 형변환(casting)
		
		// DB user.id를 FK로 설정
		commentvo.setUserFk(loginUser.getId());
		
		// service에서 DB insert + select 처리
		JYCommentVO insertedComment = detailservice.insertComment(commentvo);
		return insertedComment; 
	}
	
	// 댓글 수정
	@PutMapping("/modComment/{commentId}")
	@ResponseBody
	public JYCommentVO updateComment(@PathVariable("commentId") int commentId, 
									 @RequestBody JYCommentVO commentvo) {
		
		commentvo.setId(commentId); // PathVariable로 받은 id 설정		
		JYCommentVO updatedComment = detailservice.updateComment(commentvo);
		
		return updatedComment;
	}
	
	// 댓글 삭제
	@DeleteMapping("/delComment/{commentId}")
	@ResponseBody
	public String deleteComment(@PathVariable("commentId") int commentId) {
		detailservice.deleteComment(commentId);
		
		return "success"; // 성공 시 클라이언트에서 처리
	}
	
}