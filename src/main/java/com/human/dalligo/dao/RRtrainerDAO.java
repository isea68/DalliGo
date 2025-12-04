package com.human.dalligo.dao;

import java.util.List;

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
	
	public List<RRtrainerVO> selectAll(){
		return   sqlsession.selectOne(Mapper+".selectAll");
		
	}
	
	public RRtrainerVO selectForLogin(RRtrainerVO trainervo){
		return   sqlsession.selectOne(Mapper+".selectForLogin", trainervo);
		
	}
	
	
	public RRtrainerVO selectById(Integer id){
		return   sqlsession.selectOne(Mapper+".getBySelectId", id);
		
	}
	
	
	public List<RRtrainerVO> select(RRtrainerVO trainervo){
		return   sqlsession.selectOne(Mapper+".selectAll");
		
	}
	

}
