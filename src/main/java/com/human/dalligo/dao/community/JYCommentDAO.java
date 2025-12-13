package com.human.dalligo.dao.community;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.community.JYCommentVO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JYCommentDAO {

	private final SqlSession sqlsession;
	
	private static String MAPPER = "com.human.dalligo.dao.community.JYCommentDAO";
	
	public void insertComment(JYCommentVO commentvo) {
		sqlsession.insert(MAPPER + ".insertComment", commentvo);
	}

	public List<JYCommentVO> getCommentsById(int postId) {
		
		List<JYCommentVO> commentList = sqlsession.selectList(MAPPER + ".selectCommentsById", postId);
		return commentList;
	}
	
	public JYCommentVO getOneCommentById(int id) {
		JYCommentVO insertedComment = sqlsession.selectOne(MAPPER + ".selectOneCommentById", id);
		return insertedComment;
	}

	public void updateComment(JYCommentVO commentvo) {
		sqlsession.update(MAPPER + ".updateComment", commentvo);
		
	}

	public void deleteComment(int id) {
		sqlsession.delete(MAPPER + ".deleteComment", id);
		
	}
}
