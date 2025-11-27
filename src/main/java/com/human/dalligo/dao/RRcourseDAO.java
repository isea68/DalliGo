package com.human.dalligo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.RRcourseVO;

@Repository
public class RRcourseDAO {
	
	@Autowired
	SqlSession sqlsession;
	private static final String Mapper ="com.human.dalligo.dao.RRcourseDAO";
	
	public void insert(RRcourseVO coursevo) {
		sqlsession.insert(Mapper+".insertcourse", coursevo);
	}
	
	public List<RRcourseVO> selectAll(){
		List<RRcourseVO> course=sqlsession.selectList(Mapper+".selectAllcourse");
		return course;
	}
}
