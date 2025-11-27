package com.human.dalligo.service;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.RRtrainerDAO;
import com.human.dalligo.vo.RRtrainerVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RRtrainerService {
	
	private final RRtrainerDAO trainerdao;
	
	public void insert(RRtrainerVO trainervo) {		
		trainerdao.insert(trainervo);
	}
			
	public RRtrainerVO select(RRtrainerVO trainervo){
		RRtrainerVO tvo = trainerdao.select(trainervo);
		if (tvo!=null && tvo.getPassword().equals(trainervo.getPassword())) {
			return tvo;
		}else {
			return null ;
		}
	}

}
