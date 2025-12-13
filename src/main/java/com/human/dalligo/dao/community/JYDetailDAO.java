package com.human.dalligo.dao.community;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.community.JYDetailVO;
import com.human.dalligo.vo.community.JYFileVO;
import com.human.dalligo.vo.community.JYPostVO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JYDetailDAO {
	
	private final SqlSession sqlsession;
	
	private static String MAPPER = "com.human.dalligo.dao.community.JYDetailDAO";


	public JYDetailVO detailById(int id) {
		JYDetailVO detail = sqlsession.selectOne(MAPPER + ".selectDetailById", id);
		return detail;
	}
	
	public void update(JYPostVO postvo) {
		sqlsession.update(MAPPER + ".update", postvo);
	}
	
	public void delete(Integer id) {
		sqlsession.delete(MAPPER + ".deleteById", id);		
	}

	public List<JYFileVO> getFiles(int id) {
		List<JYFileVO> list = sqlsession.selectList(MAPPER + ".selectFilesById", id);
		return list;
	}

	public String getNicknameByUserId(int userId) {
		String nickName = sqlsession.selectOne(MAPPER + ".selectNicknameByUserId", userId);
		return nickName;
	}

	
	public List<JYFileVO> findFilesByPostId(int postId) {
		List<JYFileVO> oldFileList = sqlsession.selectList(MAPPER + ".selectFilesByPostId", postId);
		return oldFileList;
	}

	public void deleteFilesByPostId(int postId) {
		sqlsession.delete(MAPPER + ".deleteFilesByPostId", postId);
		
	}
}
