package com.human.dalligo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.ScheduleVO;



@Repository
public class ScheduleDAO {

    @Autowired
    private SqlSession sqlSession;
    private static final String MAPPER="com.human.dalligo.dao.ScheduleDAO";

    public List<ScheduleVO> selectAll(){
        return sqlSession.selectList(MAPPER + ".selectAll");
    }

    // order은 "ASC" 또는 "DESC" (대소문자 상관없음)
    public List<ScheduleVO> selectAllOrderByStartDate(String order) {
        Map<String, Object> param = new HashMap<>();
        param.put("order", order);
        return sqlSession.selectList(MAPPER + ".selectAllOrderByStartDate", param);
    }
}
