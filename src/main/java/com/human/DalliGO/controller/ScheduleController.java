package com.human.dalligo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.human.dalligo.service.ScheduleService;
import com.human.dalligo.vo.ScheduleVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService detailService;

    // JSON API
    @GetMapping("/schedule/data")
    public List<ScheduleVO> getSchedule(@RequestParam(name = "sort", defaultValue = "desc") String sort) {
        return detailService.selectAllSortedByDate(sort);
    }
}
