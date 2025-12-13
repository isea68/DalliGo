package com.human.dalligo.controller.community;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.dalligo.service.community.JYCommentService;
import com.human.dalligo.service.community.JYDetailService;
import com.human.dalligo.service.community.JYLikeService;
import com.human.dalligo.vo.JSUserVO;
import com.human.dalligo.vo.community.JYCommentVO;
import com.human.dalligo.vo.community.JYDetailVO;
import com.human.dalligo.vo.community.JYPostVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;




@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class JYDetailController {
	
	private final JYDetailService detailservice;
	private final JYCommentService commentservice;
	private final JYLikeService likeservice;
	
	// 단일 게시글 자세히 보기
	@GetMapping("/detail/{id}")
	public String getdetail(@PathVariable("id") int postId,
							@RequestParam(value = "category", required = false, defaultValue="") String category,
							@RequestParam(value = "search", required = false, defaultValue="") String search,
							HttpSession session,
							Model model) {
		
		// 게시글 정보 조회
		JYDetailVO detail = detailservice.detailById(postId);
		List<JYCommentVO> commentList = commentservice.getCommentsById(postId);
		
		model.addAttribute("detail", detail);		
		model.addAttribute("commentList", commentList);
		
		// 로그인 사용자 정보 가져오기
		JSUserVO loginUser = (JSUserVO) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		
		// 초기 하트 상태 설정 (비회원이면 false)
		boolean isLikedByUser = false;
		String userId = null;
		
		if(loginUser != null) {
			userId = loginUser.getUserId();
			isLikedByUser = likeservice.isLiked(postId, userId);
		}
		
		model.addAttribute("isLikeByUser", isLikedByUser);
		
		// 좋아요 숫자
		int countLikes = likeservice.countLikes(postId);
		model.addAttribute("countLikes", countLikes);

		
		// 뒤로가기용
		model.addAttribute("category", category);
		model.addAttribute("search", search);
		
		return "/community/detail";
	}

	
	// 수정할 단일 게시글 조회
	@GetMapping("/update/{id}")
	public String updatePost(@PathVariable("id") int id, Model model) {
		JYPostVO detailvo = detailservice.getDetailByPostId(id);
		
		model.addAttribute("detailvo", detailvo);
		return "/community/detailMod";
	}
	
	// 조회한 단일 게시글 수정 처리
	@PostMapping("/update")
	public String update(@ModelAttribute JYPostVO postvo,
						 @RequestParam(value = "category", required = false) String category,
						 @RequestParam(value = "search", required = false) String search,
						 @RequestParam(value="files", required=false) List<MultipartFile> files) throws IOException {
		

		detailservice.update(postvo, files);
		
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
	
}