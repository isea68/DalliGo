package com.human.dalligo.service.store;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.store.SSGoodsDAO;
import com.human.dalligo.vo.store.SSGoodsVO;
import com.human.dalligo.vo.store.SSOrderVO;

@Service
public class SSGoodsService {

    private final SSGoodsDAO goodsDAO;

    public SSGoodsService(SSGoodsDAO goodsDAO) {
        this.goodsDAO = goodsDAO;
    }

    public List<SSGoodsVO> getAllGoods() {
        List<SSGoodsVO> list = goodsDAO.selectAll();
        if (list == null) return Collections.emptyList();
        list.removeIf(item -> item == null);
        return list;
    }

    public SSGoodsVO getGoodsById(int goodsId) {
        return goodsDAO.getGoodsById(goodsId);
    }

    public void addGoods(SSGoodsVO goodsVO) {
        goodsDAO.insert(goodsVO);
    }

    public void updateGoods(SSGoodsVO goodsVO) {
        goodsDAO.update(goodsVO);
    }

    public void deleteGoods(int goodsId) {
        goodsDAO.delete(goodsId);
    }

    /** 🔥 필터 상품 — null-safe 버전 */
    public List<SSGoodsVO> getGoodsByFilter(String tag, String brand, Integer minPrice, Integer maxPrice) {
        if (minPrice == null) minPrice = 0;
        if (maxPrice == null) maxPrice = 300000;

        List<SSGoodsVO> list = goodsDAO.selectByFilter(brand, tag, minPrice, maxPrice);

        if (list == null) return Collections.emptyList();

        // 🔥 null 제거
        list.removeIf(item -> item == null);

        return list;
    }

    /** 🔥 TOP 상품 — null-safe 버전 */
    public List<SSGoodsVO> getTopGoods() {
        List<SSGoodsVO> list = goodsDAO.selectTopGoods();

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