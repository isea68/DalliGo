package com.human.DalliGO.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.DalliGO.vo.DetailVO;

@Repository
public class DetailDAO {

    @Autowired
    private SqlSession sqlSession;
    private static final String MAPPER="com.human.DalliGO.dao.DetailDAO";

    public List<DetailVO> selectAll(){
        return sqlSession.selectList(MAPPER + ".selectAll");
    }

    // order은 "ASC" 또는 "DESC" (대소문자 상관없음)
    public List<DetailVO> selectAllOrderByStartDate(String order) {
        Map<String, Object> param = new HashMap<>();
        param.put("order", order);
        return sqlSession.selectList(MAPPER + ".selectAllOrderByStartDate", param);
    }
}
