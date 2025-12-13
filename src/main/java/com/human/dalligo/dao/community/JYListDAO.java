package com.human.dalligo.dao.community;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.community.JYListVO;
import com.human.dalligo.vo.community.JYPostVO;

@Repository
public class JYListDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String MAPPER = "com.human.dalligo.dao.community.JYListDAO";

    // 전체 조회
    public List<JYListVO> getListAll(String category, String search) {
        return sqlSession.selectList(MAPPER + ".selectAll");
    }
    
    // 단건 조회
    public JYPostVO getListById(int id) {
        return sqlSession.selectOne(MAPPER + ".selectOneById", id);
    }
    
    // 카테고리별 조회
	public List<JYListVO> getListByCategory(String category) {
		return sqlSession.selectList(MAPPER + ".selectAllByCategory", category);
	}
	
	// 검색어 조회
	public List<JYListVO> getListBySearch(String search) {
		return sqlSession.selectList(MAPPER + ".selectAllBySearch", search);
	}
}