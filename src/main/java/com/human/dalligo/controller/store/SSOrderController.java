package com.human.dalligo.controller.store;

import com.human.dalligo.service.store.SSOrderService;
import com.human.dalligo.vo.store.SSOrderVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SSOrderController {

    private final SSOrderService orderService;

    // AJAX: 여러 상품 주문
    @PostMapping("/order")
    @ResponseBody
    public String order(@RequestBody List<SSOrderVO> orders) {
        try {
            orderService.placeOrders(orders);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    // 관리자 주문 조회
    @GetMapping("/store/admin/orderList")
    public String orderList(Model model) {
        List<SSOrderVO> orders = orderService.selectOrders();
        model.addAttribute("orders", orders);
        return "goodsshop/orderList"; // orderList.html 위치
    }
}