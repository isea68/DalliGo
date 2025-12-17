package com.human.dalligo.service.community;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.community.JYLikeDAO;
import com.human.dalligo.vo.community.JYLikeVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYLikeService {
	
	private final JYLikeDAO likedao;
	
	// 초기 좋아요 상태(0인지 1인지) 확인 (게시글 상세 초기 진입 시 초기 하트상태 조회용)
	public boolean isLiked(int postId, String userId) {
		JYLikeVO likevo = new JYLikeVO();
		likevo.setPostId(postId);
		likevo.setUserId(userId);
		return likedao.isLiked(likevo) > 0;
	}
	
	// 좋아요 있는지 없는지 확인 (사용자가 좋아요 버튼 클릭 시 상태를 보고 추가 or 삭제 로직 수행)
	public boolean toggleLike(int postId, String userId) {
		//JYLikeVO로 한번에 보내주는 작업
		JYLikeVO likevo = new JYLikeVO();
		likevo.setPostId(postId);
		likevo.setUserId(userId);
		int liked = likedao.isLiked(likevo);
		
		// 0은 좋아요가 안 눌려있어서 count = 0 로 나온 결과
		if(liked == 0) {
			likedao.insertLike(likevo);
			return true; // 좋아요 추가됨
		// 1은 좋아요가 눌려있어서 count = 1 로 나온 결과
		}else {
			likedao.deleteLike(likevo);
			return false; // 좋아요 취소됨
		}
	}
	
	// 좋아요수 조회
	public int countLikes(int postId) {
		int countLikes = likedao.countLikes(postId); 
		return countLikes;
	}

}
