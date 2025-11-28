package com.human.dalligo.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.UserVO;



@Repository
public class UserDAO {
	@Autowired
	private SqlSession sqlSession;
	private static final String MAPPER="com.human.dalligo.dao.UserDAO";
	public void insert(UserVO uservo) {
		sqlSession.insert(MAPPER+".insert",uservo);
	}
	   public UserVO getUserByUserId(String userId) {	        
	        Map<String,Object> param = new HashMap<>();
	        param.put("userId", userId);
	        return sqlSession.selectOne("com.human.DalliGO.dao.UserDAO.getUserByUserId", param);
	    }
	
}
