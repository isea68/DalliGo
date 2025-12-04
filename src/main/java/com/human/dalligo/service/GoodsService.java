package com.human.dalligo.service;

import java.util.Collections;
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
        List<GoodsVO> list = goodsDAO.selectAll();
        if (list == null) return Collections.emptyList();
        list.removeIf(item -> item == null);
        return list;
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

    /** 🔥 필터 상품 — null-safe 버전 */
    public List<GoodsVO> getGoodsByFilter(String tag, String brand, Integer minPrice, Integer maxPrice) {
        if (minPrice == null) minPrice = 0;
        if (maxPrice == null) maxPrice = 300000;

        List<GoodsVO> list = goodsDAO.selectByFilter(brand, tag, minPrice, maxPrice);

        if (list == null) return Collections.emptyList();

        // 🔥 null 제거
        list.removeIf(item -> item == null);

        return list;
    }

    /** 🔥 TOP 상품 — null-safe 버전 */
    public List<GoodsVO> getTopGoods() {
        List<GoodsVO> list = goodsDAO.selectTopGoods();

        if (list == null) return Collections.emptyList();

        // 🔥 null 제거
        list.removeIf(item -> item == null);

        return list;
    }

    /** TOP 상품 설정 */
    public void setTopGoods(int top1, int top2, int top3) {
        goodsDAO.updateTopRank(top1, top2, top3);
    }

    public List<String> getAllBrands() {
        List<String> list = goodsDAO.selectAllBrands();
        return list == null ? Collections.emptyList() : list;
    }

    public List<String> getAllTags() {
        List<String> list = goodsDAO.selectAllTags();
        return list == null ? Collections.emptyList() : list;
    }
}