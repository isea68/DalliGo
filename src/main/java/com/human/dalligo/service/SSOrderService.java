package com.human.dalligo.service;

import com.human.dalligo.dao.SSOrderDAO;
import com.human.dalligo.dao.SSGoodsDAO;
import com.human.dalligo.vo.SSOrderVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SSOrderService {

    private final SSOrderDAO orderDAO;
    private final SSGoodsDAO goodsDAO;

    // 여러 주문 처리
    public void placeOrders(List<SSOrderVO> orderList) {
        for(SSOrderVO vo : orderList) {
            orderDAO.insertOrder(vo);
            goodsDAO.decreaseStock(vo.getGoodsId(), vo.getQuantity());
        }
    }

    public List<SSOrderVO> selectOrders() {
        return orderDAO.selectOrders();
    }
}