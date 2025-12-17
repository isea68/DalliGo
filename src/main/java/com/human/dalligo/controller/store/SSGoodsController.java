package com.human.dalligo.controller.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.dalligo.service.store.SSGoodsService;
import com.human.dalligo.service.store.SSOrderService;
import com.human.dalligo.service.user.JSUserService;
import com.human.dalligo.vo.store.SSGoodsVO;
import com.human.dalligo.vo.store.SSOrderVO;
import com.human.dalligo.vo.user.JSUserVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class SSGoodsController {

    private final SSGoodsService goodsService;
    private final SSOrderService orderService;
    private final JSUserService userService;

    public SSGoodsController(SSGoodsService goodsService, SSOrderService orderService,JSUserService userService) {
        this.goodsService = goodsService;
		this.orderService = orderService;
		this.userService = userService;
    }

    // 일반 유저 상품 페이지
    @GetMapping("/store")
    public String goodsPage(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "minPrice", required = false, defaultValue = "0") int minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "300000") int maxPrice,
            Model model) {

        List<SSGoodsVO> goodsList = goodsService.getGoodsByFilter(category, brand, minPrice, maxPrice);
        List<SSGoodsVO> topGoodsList = goodsService.getTopGoods();
        if (topGoodsList == null) topGoodsList = new ArrayList<>();
        List<String> brandList = goodsService.getAllBrands();
        List<String> tagList = goodsService.getAllTags();

        model.addAttribute("goodsList", goodsList);
        model.addAttribute("topGoodsList", topGoodsList);
        model.addAttribute("brandList", brandList);
        model.addAttribute("tagList", tagList);

        return "goodsshop/goods"; // template 경로
    }

    // 관리자 페이지
    @GetMapping("/store/admin")
    public String adminPage(Model model) {
        List<SSGoodsVO> goodsList = goodsService.getAllGoods();
        model.addAttribute("goodsList", goodsList);
        return "goodsshop/admin";
    }

    // 관리자: 상품 등록
    @PostMapping("/store/admin/add")
    public String addGoods(SSGoodsVO goodsVO) {
        goodsService.addGoods(goodsVO);
        return "redirect:/store/admin";
    }

    // 관리자: 상품 수정
    @PostMapping("/store/admin/update")
    public String updateGoods(SSGoodsVO goodsVO) {
        goodsService.updateGoods(goodsVO);
        return "redirect:/store/admin";
    }

    // 관리자: 상품 삭제
    @PostMapping("/store/admin/delete")
    public String deleteGoods(@RequestParam("goodsId") int goodsId) {
        goodsService.deleteGoods(goodsId);
        return "redirect:/store/admin";
    }

    // 필터 검색
//    @GetMapping("/store/filter")
//    public String filterGoods(
//            @RequestParam(value = "category", required = false) String category,
//            @RequestParam(value = "brand", required = false) String brand,
//            @RequestParam(value = "minPrice", required = false, defaultValue = "0") int minPrice,
//            @RequestParam(value = "maxPrice", required = false, defaultValue = "300000") int maxPrice,
//            Model model) {
//
//        List<GoodsVO> filteredGoods = goodsService.getGoodsByFilter(category, brand, minPrice, maxPrice);
//        model.addAttribute("goodsList", filteredGoods);
//
//        // 브랜드와 태그 리스트도 보내서 화면 필터 유지
//        model.addAttribute("brandList", goodsService.getAllBrands());
//        model.addAttribute("tagList", goodsService.getAllTags());
//
//        return "redirect:/store?category=" + category + "&brand=" + brand + "&minPrice=" + minPrice + "&maxPrice=" + maxPrice;
//    }

    // 관리자: topGoods 설정
    @PostMapping("/store/admin/topGoods")
    public String topGoodsApply(
            @RequestParam("top1") Integer top1,
            @RequestParam("top2") Integer top2,
            @RequestParam("top3") Integer top3) {

        goodsService.setTopGoods(top1, top2, top3);
        return "redirect:/store/admin";
    }
    @GetMapping("/goodsadminlogin")
    public String loginadmin() {
    	return "goodsshop/goodsadminlogin";
    }
    @GetMapping("/store/admin/orders")
    public String orderList(Model model) {
        List<SSOrderVO> orders = orderService.selectOrders(); // SSOrderVO 사용
        model.addAttribute("orders", orders);
        return "goodsshop/orderList";
    }
    @PostMapping("/storelogin")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        JSUserVO user = userService.login(userId, password);
        if(user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/store";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "redirect:/store";
        }
    }
    @GetMapping("/storelogout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화 → 로그아웃
        return "redirect:/store";
    }
    

}