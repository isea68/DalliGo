package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.RRcourseDAO;
import com.human.dalligo.vo.RRcourseVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RRcourseService {
	
	private final RRcourseDAO coursedao;
	
	public void insert(RRcourseVO coursevo) {
		coursedao.insert(coursevo);
		
	}
	            
	public List<RRcourseVO> selectList() {
		List<RRcourseVO> courses=coursedao.selectAll();	
		return  courses;
		
	}
	
	public RRcourseVO selectById(Integer id) {
		RRcourseVO course = coursedao.selectById(id);
		return course;
	}

}
