package com.human.dalligo.dao;


import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.human.dalligo.vo.LshEventVO;


@Mapper
public interface LshEventDAO {
	
	// 전체 이벤트 조회
	List<LshEventVO> selectAll();
	// id로 단일 이벤트 조회
	LshEventVO selectOne(int id);

}
