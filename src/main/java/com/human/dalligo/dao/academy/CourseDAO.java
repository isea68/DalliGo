package com.human.dalligo.dao.academy;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.academy.CourseVO;



@Repository
public class CourseDAO {
	
	@Autowired
	SqlSession sqlsession;
	private static final String Mapper ="com.human.dalligo.dao.academy.CourseMapper";
	
	public void insert(CourseVO coursevo) {
		sqlsession.insert(Mapper+".insertcourse", coursevo);
	}
	
	public List<CourseVO> selectAll(){
		List<CourseVO> courses=sqlsession.selectList(Mapper+".selectAllcourse");
		return courses;
	}
	
	public CourseVO selectById(Integer id) {
		CourseVO course = sqlsession.selectOne(Mapper+".selectById",id);
		return course;
	}

	public void delete(Integer id) {;
		sqlsession.delete(Mapper+".deleteCourse", id);
		
	}
	public int deleteint(Integer id) {;
	return sqlsession.delete(Mapper+".deleteCourse", id);
	
}

	public List<CourseVO> findByTrainerId(Integer trainerPk) {
		return sqlsession.selectList(Mapper+".findByTrainerId", trainerPk);
	}
	
	

}
