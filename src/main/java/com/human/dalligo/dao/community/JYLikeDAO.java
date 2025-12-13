package com.human.dalligo.dao.community;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.community.JYLikeVO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JYLikeDAO {

	private final SqlSession sqlsession;
	
	private static String MAPPER = "com.human.dalligo.dao.community.JYLikeDAO";
	
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
