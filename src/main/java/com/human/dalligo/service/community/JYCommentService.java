package com.human.dalligo.service.community;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.community.JYCommentDAO;
import com.human.dalligo.vo.community.JYCommentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYCommentService {

	private final JYCommentDAO commentdao;
	
	// 댓글 달기
	public JYCommentVO insertComment(JYCommentVO commentvo) {
		commentdao.insertComment(commentvo);
		
		// DB에서 insertComment한 DB 정보 불러와서 세팅해서 Controller에 전달
		// insert 시 id, inDate가 자동 세팅된다면 commentvo에 이미 들어있음 - MAPPER에서 useGeneratedKeys="true" keyProperty="id"
		JYCommentVO insertedComment = commentdao.getOneCommentById(commentvo.getId());
		
		return insertedComment;
	}

	// 댓글 목록 조회
	public List<JYCommentVO> getCommentsById(int postId) {
		List<JYCommentVO> commentList = commentdao.getCommentsById(postId);
		return commentList;
	}

	// 댓글 수정
	public JYCommentVO updateComment(JYCommentVO commentvo) {
		// 댓글 내용 업데이트 - commentId와 content값만 있음, userFk와 nickName = null인 상태
		commentdao.updateComment(commentvo);
		
		// 수정된 댓글을 DB에서 다시 조회해서 반환 (userFk와 nickName도 view에 반환해줘야 하기 때문에 이 작업이 필요)
		JYCommentVO updatedComment = commentdao.getOneCommentById(commentvo.getId());
		return updatedComment;
	}

	// 댓글 삭제
	public void deleteComment(int id) {
		commentdao.deleteComment(id);
		
	}
}
