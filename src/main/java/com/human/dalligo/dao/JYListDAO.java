package com.human.dalligo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.JYListVO;
import com.human.dalligo.vo.JYPostVO;

@Repository
public class JYListDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String MAPPER = "com.human.dalligo.dao.JYListDAO";

    public List<JYListVO> getListByCategory(String category) {
        return sqlSession.selectList(MAPPER + ".selectAllByCategory", category);
    }

    public List<JYListVO> getListAll() {
        return sqlSession.selectList(MAPPER + ".selectAll");
    }
    
    // 단건 조회
    public JYPostVO getListById(int id) {
        return sqlSession.selectOne(MAPPER + ".selectOneById", id);
    }

    public List<JYListVO> findListByPage(int offset, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        return sqlSession.selectList(MAPPER + ".selectByPage", params);
    }

    public int countList() {
        return sqlSession.selectOne(MAPPER + ".countList");
    }
    
    // 카테고리별 게시물 조회 (페이징)
	public List<JYListVO> findListByCategoryPage(String category, int offset, int pageSize) {
		Map<String, Object> params = new HashMap<>();
		params.put("category", category);
		params.put("offset", offset);
		params.put("pageSize", pageSize);
		return sqlSession.selectList(MAPPER + ".selectAllByCategory", params);
	}

	public int countByCategory(String category) {
		return sqlSession.selectOne(MAPPER + ".countByCategory", category);
	}
}