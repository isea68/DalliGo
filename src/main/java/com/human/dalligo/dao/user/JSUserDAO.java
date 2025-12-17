package com.human.dalligo.dao.user;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.user.JSUserVO;

@Repository
public class JSUserDAO {

	@Autowired
	private SqlSession sqlSession;

	private static final String MAPPER = "com.human.dalligo.dao.user.JSUserDAO";

	// 회원가입
	public void insert(JSUserVO uservo) {
		sqlSession.insert(MAPPER + ".insert", uservo);
	}

	// 아이디로 조회
	public JSUserVO getUserByUserId(String userId) {
		return sqlSession.selectOne(MAPPER + ".getUserByUserId", userId);
	}

	// 닉네임으로 조회 (아이디와 동일하게 String 사용)
	public JSUserVO getUserByNickName(String nickName) {
		return sqlSession.selectOne(MAPPER + ".getUserByNickName", nickName);
	}	

	public void updateUser(JSUserVO user) {
		sqlSession.update(MAPPER + ".updateUser", user);
	}

	public void deleteUser(String userId) {
		sqlSession.delete(MAPPER + ".deleteUser", userId);
	}

}
