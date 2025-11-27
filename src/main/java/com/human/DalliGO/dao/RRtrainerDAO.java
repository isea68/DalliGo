package com.human.dalligo.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.RRtrainerVO;

@Repository
public class RRtrainerDAO {
	
	@Autowired
	SqlSession sqlsession;
	private static final String Mapper ="com.human.dalligo.dao.RRtrainerDAO";
	
	public void insert(RRtrainerVO trainervo) {
		sqlsession.insert(Mapper+".insert", trainervo);
		
		System.out.println(trainervo);
	}
	
	public RRtrainerVO select(RRtrainerVO trainervo){
		return   sqlsession.selectOne(Mapper+".getBySelectId", trainervo);
		
	}

}
