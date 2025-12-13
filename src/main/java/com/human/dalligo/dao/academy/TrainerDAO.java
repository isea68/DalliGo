package com.human.dalligo.dao.academy;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.human.dalligo.vo.academy.TrainerVO;

@Repository
public class TrainerDAO {

	@Autowired
	SqlSession sqlsession;
	private static final String Mapper ="com.human.dalligo.dao.academy.TrainerDAO";
	
	public void insert(TrainerVO trainervo) {
		sqlsession.insert(Mapper+".insert", trainervo);
		
		System.out.println(trainervo);
	}
	
	public List<TrainerVO> selectAll(){
		return   sqlsession.selectOne(Mapper+".selectAll");
		
	}
	
	public TrainerVO selectForLogin(TrainerVO trainervo){
		return   sqlsession.selectOne(Mapper+".selectForLogin", trainervo);
		
	}
	
	
	public TrainerVO selectById(Integer id){
		return   sqlsession.selectOne(Mapper+".getBySelectId", id);
		
	}
	
	
	public List<TrainerVO> select(TrainerVO trainervo){
		return   sqlsession.selectOne(Mapper+".selectAll");
		
	}
	

}
