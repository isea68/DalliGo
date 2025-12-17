package com.human.dalligo.dao.academy;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.academy.RRcourseVO;

@Repository
public class RRcourseDAO {
	
	@Autowired
	SqlSession sqlsession;
	private static final String Mapper ="com.human.dalligo.dao.academy.RRcourseMapper";
	
	public void insert(RRcourseVO coursevo) {
		sqlsession.insert(Mapper+".insertcourse", coursevo);
	}
	
	public List<RRcourseVO> selectAll(){
		List<RRcourseVO> courses=sqlsession.selectList(Mapper+".selectAllcourse");
		return courses;
	}
	
	public RRcourseVO selectById(Integer id) {
		RRcourseVO course = sqlsession.selectOne(Mapper+".selectById",id);
		return course;
	}

	public void delete(Integer id) {;
		sqlsession.delete(Mapper+".deleteCourse", id);
		
	}
	public int deleteint(Integer id) {;
	return sqlsession.delete(Mapper+".deleteCourse", id);
	
}

	public List<RRcourseVO> findByTrainerId(Integer trainerPk) {
		return sqlsession.selectList(Mapper+".findByTrainerId", trainerPk);
	}
	
	
}
