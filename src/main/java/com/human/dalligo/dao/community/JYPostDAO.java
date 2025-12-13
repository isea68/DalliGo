package com.human.dalligo.dao.community;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.community.JYFileVO;
import com.human.dalligo.vo.community.JYPostVO;

@Repository
public class JYPostDAO {

	@Autowired
	SqlSession sqlsession;
	
	private static String MAPPER = "com.human.dalligo.dao.community.JYPostDAO";
	
	public void insert(JYPostVO postvo) {
		sqlsession.insert(MAPPER + ".insert", postvo);
	}
	
	public JYPostVO getDetailByPostId(int postId) {
		JYPostVO detailvo = sqlsession.selectOne(MAPPER + ".selectOneByPostId", postId);
		return detailvo;
	}


	public void insertFiles(JYFileVO filevo) {
		sqlsession.insert(MAPPER + ".insertFiles", filevo);
		
	}
}