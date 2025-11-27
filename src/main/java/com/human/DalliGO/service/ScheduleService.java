package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.ScheduleDAO;
import com.human.dalligo.vo.ScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleDAO detaildao;

    // 기존 메소드 유지(필요하면)
    public List<ScheduleVO> selectAll(){
        return detaildao.selectAll();
    }

    // 정렬값("asc" 또는 "desc") 받아 DB에서 정렬해서 리턴
    public List<ScheduleVO> selectAllSortedByDate(String sort) {
        String order = "desc".equalsIgnoreCase(sort) ? "DESC" : "ASC";
        return detaildao.selectAllOrderByStartDate(order);
    }
}
