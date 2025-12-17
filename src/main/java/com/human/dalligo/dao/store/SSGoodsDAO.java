package com.human.dalligo.dao.store;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.human.dalligo.vo.store.SSGoodsVO;

@Repository
public class SSGoodsDAO {

    @Autowired
    private SqlSession sqlSession;

    // Mapper namespace
    private static final String MAPPER = "com.human.dalligo.dao.store.SSGoodsDAO";

    // 전체 상품 조회
    public List<SSGoodsVO> selectAll() {
        return sqlSession.selectList(MAPPER + ".selectAll");
    }

    // 상품 id로 조회
    public SSGoodsVO getGoodsById(int goodsId) {
        return sqlSession.selectOne(MAPPER + ".getGoodsById", goodsId);
    }

    // 상품 등록
    public void insert(SSGoodsVO goodsVO) {
        sqlSession.insert(MAPPER + ".insert", goodsVO);
    }

    // 상품 수정
    public void update(SSGoodsVO goodsVO) {
        sqlSession.update(MAPPER + ".update", goodsVO);
    }

    // 상품 삭제
    public void delete(int goodsId) {
        sqlSession.delete(MAPPER + ".delete", goodsId);
    }

    // 필터 조건으로 조회 (가격, 카테고리, 브랜드)
    public List<SSGoodsVO> selectByFilter(String brand, String tag, int minPrice, int maxPrice) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("brand", brand);
        params.put("tag", tag);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
     
        return sqlSession.selectList(MAPPER + ".selectByFilter", params);
        
    }
 // TOP 상품 조회
    public List<SSGoodsVO> selectTopGoods() {
        return sqlSession.selectList(MAPPER + ".selectTopGoods");
    }

    // TOP 상품 순위 업데이트
    public void updateTopRank(int top1, int top2, int top3) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("top1", top1);
        params.put("top2", top2);
        params.put("top3", top3);
        sqlSession.update(MAPPER + ".updateTopRank", params);
    }
    // 브랜드 전체 조회
    public List<String> selectAllBrands() {
        return sqlSession.selectList(MAPPER + ".selectAllBrands");
    }

    // 태그 전체 조회
    public List<String> selectAllTags() {
        return sqlSession.selectList(MAPPER + ".selectAllTags");
    }
    public void decreaseStock(int goodsId, int quantity) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        params.put("quantity", quantity);
        sqlSession.update(MAPPER + ".decreaseStock", params);
    }
    
}
