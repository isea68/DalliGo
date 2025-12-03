package com.human.dalligo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.dalligo.service.GoodsService;
import com.human.dalligo.vo.GoodsVO;

@Controller
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 일반 유저 상품 페이지
    @GetMapping("/store")
    public String goodsPage(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "minPrice", required = false, defaultValue = "0") int minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "300000") int maxPrice,
            Model model) {

        List<GoodsVO> goodsList = goodsService.getGoodsByFilter(category, brand, minPrice, maxPrice);
        List<GoodsVO> topGoodsList = goodsService.getTopGoods();
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
        List<GoodsVO> goodsList = goodsService.getAllGoods();
        model.addAttribute("goodsList", goodsList);
        return "goodsshop/admin";
    }

    // 관리자: 상품 등록
    @PostMapping("/store/admin/add")
    public String addGoods(GoodsVO goodsVO) {
        goodsService.addGoods(goodsVO);
        return "redirect:/store/admin";
    }

    // 관리자: 상품 수정
    @PostMapping("/store/admin/update")
    public String updateGoods(GoodsVO goodsVO) {
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
    @GetMapping("/login")
    public String loginadmin() {
    	return "login";
    }
    

}