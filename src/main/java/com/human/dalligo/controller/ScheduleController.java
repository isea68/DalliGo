package com.human.dalligo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.dalligo.service.ScheduleService;
import com.human.dalligo.vo.ScheduleVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService detailService;

    // /detail?sort=desc 또는 /detail?sort=asc
    @GetMapping("/schedule")
    public String getDetail(@RequestParam(name = "sort", defaultValue = "desc") String sort, Model model) {
        // 서비스에서 정렬값 검증/처리
        List<ScheduleVO> events = detailService.selectAllSortedByDate(sort);
        model.addAttribute("events", events);
        model.addAttribute("sort", sort);
        return "schedule";
    }
}
