package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.GoodsDAO;
import com.human.dalligo.vo.GoodsVO;

@Service
public class GoodsService {

    private final GoodsDAO goodsDAO;

    public GoodsService(GoodsDAO goodsDAO) {
        this.goodsDAO = goodsDAO;
    }

    public List<GoodsVO> getAllGoods() {
        return goodsDAO.selectAll();
    }

    public GoodsVO getGoodsById(int goodsId) {
        return goodsDAO.getGoodsById(goodsId);
    }

    public void addGoods(GoodsVO goodsVO) {
        goodsDAO.insert(goodsVO);
    }

    public void updateGoods(GoodsVO goodsVO) {
        goodsDAO.update(goodsVO);
    }

    public void deleteGoods(int goodsId) {
        goodsDAO.delete(goodsId);
    }

    public List<GoodsVO> getGoodsByFilter(String tag, String brand, Integer minPrice, Integer maxPrice) {
        if(minPrice == null) minPrice = 0;
        if(maxPrice == null) maxPrice = 300000;
        return goodsDAO.selectByFilter(brand, tag, minPrice, maxPrice);
    }
 // TOP 상품 가져오기
    public List<GoodsVO> getTopGoods() {
        return goodsDAO.selectTopGoods(); // Mapper에서 정의 필요
    }

    // TOP 상품 설정
    public void setTopGoods(int top1, int top2, int top3) {
        goodsDAO.updateTopRank(top1, top2, top3); // Mapper에서 정의 필요
    }
    public List<String> getAllBrands() {
        return goodsDAO.selectAllBrands();
    }

    public List<String> getAllTags() {
        return goodsDAO.selectAllTags();
    }
   
    
}