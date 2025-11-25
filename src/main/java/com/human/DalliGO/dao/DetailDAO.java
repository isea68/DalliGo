package com.human.DalliGO.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.DalliGO.vo.DetailVO;

@Repository
public class DetailDAO {

	@Autowired
	SqlSession sqlSession;
	private static final String MAPPER="com.human.DalliGO.dao.DetailDAO";
	public List<DetailVO> selectAll(){
		List<DetailVO> events = 
				sqlSession.selectList(MAPPER+".selectAll");;
		return events;
	}
}
