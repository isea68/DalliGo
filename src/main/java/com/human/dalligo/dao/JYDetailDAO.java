package com.human.dalligo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.FileVO;
import com.human.dalligo.vo.JYDetailVO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JYDetailDAO {
	
	private final SqlSession sqlsession;
	
	private static String MAPPER = "com.human.dalligo.dao.JYDetailDAO";


	public JYDetailVO detailById(int id) {
		JYDetailVO detail = sqlsession.selectOne(MAPPER + ".selectOne", id);
		return detail;
	}
	
	public void delete(Integer id) {
		sqlsession.delete(MAPPER + ".delete", id);		
	}

	public List<FileVO> getFiles(int id) {
		List<FileVO> list = sqlsession.selectList(MAPPER + ".select_files", id);
		return list;
	}
	
}
