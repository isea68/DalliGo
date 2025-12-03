package com.human.dalligo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.human.dalligo.dao.JYDetailDAO;
import com.human.dalligo.dao.JYPostDAO;
import com.human.dalligo.vo.JYFileVO;
import com.human.dalligo.vo.JYCommentVO;
import com.human.dalligo.vo.JYDetailVO;
import com.human.dalligo.vo.JYLikeVO;
import com.human.dalligo.vo.JYPostVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYDetailService {

	private final JYDetailDAO detaildao;
	private final JYPostDAO postdao;

	@Transactional(readOnly = true)
	public JYDetailVO detailById(int id) {
		JYDetailVO detail = detaildao.detailById(id);
		if(detail == null) {
			return null;
		}else {
			List<JYFileVO> files = detaildao.getFiles(id);
			detail.setFileList(files);
		}
		return detail;
	}
	
	// 단일 게시글 자세히 보기
	public JYPostVO getDetailById(int id) {
		JYPostVO detailvo = postdao.getDetailById(id);
		return detailvo;
	}

	// 단일 게시글 수정
	public void update(JYPostVO postvo) {
		postdao.update(postvo);
	}
	
	// 단일 게시글 삭제
	public void delete(Integer id) {
		detaildao.delete(id);
	}

	// 댓글 달기
	public JYCommentVO insertComment(JYCommentVO commentvo) {
		detaildao.insertComment(commentvo);
		
		// DB에서 insertComment한 DB 정보 불러와서 세팅해서 Controller에 전달
		// insert 시 id, inDate가 자동 세팅된다면 commentvo에 이미 들어있음 - MAPPER에서 useGeneratedKeys="true" keyProperty="id"
		JYCommentVO insertedComment = detaildao.getOneCommentById(commentvo.getId());
		
		return insertedComment;
	}


	public List<JYCommentVO> getCommentsById(int postId) {
		List<JYCommentVO> commentList = detaildao.getCommentsById(postId);
		return commentList;
	}

	// 댓글 수정
	public JYCommentVO updateComment(JYCommentVO commentvo) {
		detaildao.updateComment(commentvo);
		
		// 수정 후 닉네임과 수정 시간 세팅
		String userId = detaildao.getNicknameByUserId(commentvo.getUserFk());
		commentvo.setNickName(userId);
		commentvo.setInDate(LocalDateTime.now());
		return commentvo;
	}

	// 댓글 삭제
	public void deleteComment(int id) {
		detaildao.deleteComment(id);
		
	}
	
	// 초기 좋아요수 상태 확인
	public boolean isLiked(int postId, String userId) {
		JYLikeVO likevo = new JYLikeVO();
		likevo.setPostId(postId);
		likevo.setUserId(userId);
		return detaildao.isLiked(likevo) > 0;
	}
	
	// 좋아요 있는지 없는지 확인
	public boolean toggleLike(int postId, String userId) {
		//JYLikeVO로 한번에 보내주는 작업
		JYLikeVO likevo = new JYLikeVO();
		likevo.setPostId(postId);
		likevo.setUserId(userId);
		int liked = detaildao.isLiked(likevo);
		
		// 0은 좋아요가 안 눌려있어서 count = 0 로 나온 결과
		if(liked == 0) {
			detaildao.insertLike(likevo);
			return true; // 좋아요 추가됨
		// 1은 좋아요가 눌려있어서 count = 1 로 나온 결과
		}else {
			detaildao.deleteLike(likevo);
			return false; // 좋아요 취소됨
		}
	}
	
	// 좋아요수 조회
	public int countLikes(int postId) {
		int countLikes = detaildao.countLikes(postId); 
		return countLikes;
	}
}
