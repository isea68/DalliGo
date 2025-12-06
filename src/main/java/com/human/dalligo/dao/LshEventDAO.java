package com.human.dalligo.dao;


import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.human.dalligo.vo.LshEventVO;


@Mapper
public interface LshEventDAO {
	List<LshEventVO> selectAll();

	LshEventVO selectOne(int id);

	//int insert(LshEventVO vo);
	
	LshEventVO selectEventById(int id);

	LocalDateTime getStartDate();

}
