package com.human.dalligo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.JYFileVO;
import com.human.dalligo.vo.JYCommentVO;
import com.human.dalligo.vo.JYDetailVO;
import com.human.dalligo.vo.JYLikeVO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JYDetailDAO {
	
	private final SqlSession sqlsession;
	
	private static String MAPPER = "com.human.dalligo.dao.JYDetailDAO";


	public JYDetailVO detailById(int id) {
		JYDetailVO detail = sqlsession.selectOne(MAPPER + ".selectDetailById", id);
		return detail;
	}
	
	public void delete(Integer id) {
		sqlsession.delete(MAPPER + ".deleteById", id);		
	}

	public List<JYFileVO> getFiles(int id) {
		List<JYFileVO> list = sqlsession.selectList(MAPPER + ".selectFilesById", id);
		return list;
	}

	public void insertComment(JYCommentVO commentvo) {
		sqlsession.insert(MAPPER + ".insertComment", commentvo);
	}

	public List<JYCommentVO> getCommentsById(int postId) {
		
		List<JYCommentVO> commentList = sqlsession.selectList(MAPPER + ".selectCommentsById", postId);
		return commentList;
	}

	public String getNicknameByUserId(int userId) {
		String nickName = sqlsession.selectOne(MAPPER + ".selectNicknameByUserId", userId);
		return nickName;
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
	
	// post_likes에 user_id기반 좋아요가 있는지 검사
	public int isLiked(JYLikeVO likevo) {
		int liked = sqlsession.selectOne(MAPPER + ".isLikedByUserId", likevo);
		return liked;
	}
	
	// post_likes에 user_id기반 좋아요 insert
	public void insertLike(JYLikeVO likevo) {
		sqlsession.insert(MAPPER + ".insertLike", likevo);
	}
	
	// post_likes에 user_id기반 좋아요 delete
	public void deleteLike(JYLikeVO likevo) {
		sqlsession.delete(MAPPER + ".deleteLike", likevo);
		
	}

	public int countLikes(int postId) {
		int countLikes = sqlsession.selectOne(MAPPER + ".selectCountLikes", postId);
		return countLikes;
	}
}
