package com.human.dalligo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.SSOrderVO;

@Repository
public class SSOrderDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String MAPPER = "com.human.dalligo.dao.SSOrderDAO";

    public void insertOrder(SSOrderVO vo) {
        sqlSession.insert(MAPPER + ".insertOrder", vo);
    }

    public List<SSOrderVO> selectOrders() {
        return sqlSession.selectList(MAPPER + ".selectOrders");
    }
}