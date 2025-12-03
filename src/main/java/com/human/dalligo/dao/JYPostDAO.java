package com.human.dalligo.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.JYFileVO;
import com.human.dalligo.vo.JYPostVO;

@Repository
public class JYPostDAO {

	@Autowired
	SqlSession sqlsession;
	
	private static String MAPPER = "com.human.dalligo.dao.JYPostDAO";
	
	public void insert(JYPostVO postvo) {
		sqlsession.insert(MAPPER + ".insert", postvo);
	}
	
	public JYPostVO getDetailById(int id) {
		JYPostVO detailvo = sqlsession.selectOne(MAPPER + ".selectOneById", id);
		return detailvo;
	}

	public void update(JYPostVO postvo) {
		sqlsession.update(MAPPER + ".update", postvo);
	}


	public void insertFiles(JYFileVO filevo) {
		sqlsession.insert(MAPPER + ".insertFiles", filevo);
		
	}
}
