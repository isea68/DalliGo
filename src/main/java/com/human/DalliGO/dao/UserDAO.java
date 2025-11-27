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

    private static final String MAPPER = "com.human.dalligo.dao.UserDAO";

    // 회원가입
    public void insert(UserVO uservo) {
        sqlSession.insert(MAPPER + ".insert", uservo);
    }

    // 아이디로 유저 조회
    public UserVO getUserByUserId(String userId) {
        System.out.println("[DEBUG] UserDAO.getUserByUserId called with userId = " + userId);
        Map<String,Object> param = new HashMap<>();
        param.put("userId", userId);
        UserVO user = sqlSession.selectOne(MAPPER + ".getUserByUserId", param);
        System.out.println("[DEBUG] UserDAO.getUserByUserId returned = " + user);
        return user;
    }

    // ================== 아이디 존재 여부 ==================
    public boolean existsByUserId(String userId) {
        return getUserByUserId(userId) != null;
    }

    // ================== 닉네임 존재 여부 ==================
    public boolean existsByNickName(String nickName) {
        Map<String,Object> param = new HashMap<>();
        param.put("nickName", nickName);
        return sqlSession.selectOne(MAPPER + ".getUserByNickName", param) != null;
    }
    
    
}
