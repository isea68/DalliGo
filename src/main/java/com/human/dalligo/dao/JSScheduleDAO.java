package com.human.dalligo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.JSScheduleVO;

@Repository
public class JSScheduleDAO {

	@Autowired
	private SqlSession sqlSession;
	private static final String MAPPER = "com.human.dalligo.dao.JSScheduleDAO";

	public List<JSScheduleVO> selectAll() {
		return sqlSession.selectList(MAPPER + ".selectAll");
	}

	// order은 "ASC" 또는 "DESC" (대소문자 상관없음)
	public List<JSScheduleVO> selectAllOrderByStartDate(String order) {
		Map<String, Object> param = new HashMap<>();
		param.put("order", order);
		return sqlSession.selectList(MAPPER + ".selectAllOrderByStartDate", param);
	}

	public List<JSScheduleVO> selectFutureEvents() {
		return sqlSession.selectList(MAPPER + ".selectFutureEvents");
	}
}
