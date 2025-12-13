package com.human.dalligo.controller.community;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.human.dalligo.service.community.JYLikeService;
import com.human.dalligo.vo.JSUserVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class JYLikeController {
	
	private final JYLikeService likeservice;

	// 좋아요 토글
	@PostMapping("/post/{postId}/like")
	@ResponseBody
	public Map<String, Object> toggleLike(@PathVariable("postId") int postId,
										  @SessionAttribute("loginUser") JSUserVO loginUser,
										  Model model) {
		
		String userId = loginUser.getUserId(); // UserVO에서 userId 가져오기
		boolean isLiked = likeservice.toggleLike(postId, userId);
		int likeCount = likeservice.countLikes(postId);
		
		Map<String, Object> result = new HashMap<>();
		result.put("liked", isLiked);
		result.put("count", likeCount);

		return result;
	}
	
}
