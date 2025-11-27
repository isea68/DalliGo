package com.human.DalliGO.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.DalliGO.service.DetailService;
import com.human.DalliGO.vo.DetailVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DetailController {
    private final DetailService detailService;

    // /detail?sort=desc 또는 /detail?sort=asc
    @GetMapping("/detail")
    public String getDetail(@RequestParam(name = "sort", defaultValue = "desc") String sort, Model model) {
        // 서비스에서 정렬값 검증/처리
        List<DetailVO> events = detailService.selectAllSortedByDate(sort);
        model.addAttribute("events", events);
        model.addAttribute("sort", sort);
        return "detail";
    }
}
