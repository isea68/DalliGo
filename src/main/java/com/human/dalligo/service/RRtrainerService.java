package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.RRtrainerDAO;
import com.human.dalligo.vo.RRcourseVO;
import com.human.dalligo.vo.RRtrainerVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RRtrainerService {
	
	private final RRtrainerDAO trainerdao;
	
	public void insert(RRtrainerVO trainervo) {		
		trainerdao.insert(trainervo);
	}
			
//	public RRtrainerVO select(RRtrainerVO trainervo){
//		RRtrainerVO tvo = trainerdao.select(trainervo);
//		if (tvo!=null && tvo.getPassword().equals(trainervo.getPassword())) {
//			return tvo;
//		}else {
//			return null ;
//		}
//	}
//	
	
	
	public List<RRtrainerVO> selectList(){
		List<RRtrainerVO> trainervo = trainerdao.selectAll();	
		return  trainervo;		
	}	
	

	
	public RRtrainerVO selectForLogin( RRtrainerVO trainervo ) {
		RRtrainerVO tvo = trainerdao.selectForLogin(trainervo);
		if (tvo!=null && tvo.getPassword().equals(tvo.getPassword())) {
			return tvo;
		}else {
			return null ;
		}			
	}
	
	public RRtrainerVO select(Integer id) {
		RRtrainerVO tvo = trainerdao.selectById(id);
		
		return tvo;
	}

	
	

}
