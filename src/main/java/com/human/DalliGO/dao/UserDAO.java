package com.human.DalliGO.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.DalliGO.vo.UserVO;

@Repository
public class UserDAO {
	@Autowired
	SqlSession sqlSession;
	private static final String MAPPER="com.human.DalliGO.dao.UserDAO";
	public void insert(UserVO uservo) {
		sqlSession.insert(MAPPER+".insert",uservo);
	}
}
