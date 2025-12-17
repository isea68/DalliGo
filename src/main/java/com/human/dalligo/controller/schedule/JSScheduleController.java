package com.human.dalligo.controller.schedule;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.dalligo.service.schedule.JSScheduleService;
import com.human.dalligo.vo.schedule.JSScheduleVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class JSScheduleController {

    private final JSScheduleService detailService;

    @GetMapping("/schedule")

    public String schedulePage(@RequestParam(name = "sort", defaultValue = "desc") String sort, Model model) {
        // 서비스에서 일정 데이터를 가져와 모델에 넣기
        model.addAttribute("events", detailService.selectAllSortedByDate(sort));
        model.addAttribute("sort", sort); // 정렬 버튼 활성화용
        return "schedule"; // templates/schedule/list.html
    }
    
    // JSON API
    @GetMapping("/schedule/data")
    @ResponseBody
    public List<JSScheduleVO> getSchedule(@RequestParam(name = "sort", defaultValue = "desc") String sort) {
        return detailService.selectAllSortedByDate(sort);
    }
    
    
}
