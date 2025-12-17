package com.human.dalligo.service.schedule;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.schedule.JSScheduleDAO;
import com.human.dalligo.vo.schedule.JSScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JSScheduleService {
    private final JSScheduleDAO detaildao;
   
    public List<JSScheduleVO> selectAll(){
        return detaildao.selectAll();
    }

    // 정렬값("asc" 또는 "desc") 받아 DB에서 정렬해서 리턴
    public List<JSScheduleVO> selectAllSortedByDate(String sort) {

        if ("future".equalsIgnoreCase(sort)) {
            return detaildao.selectFutureEvents(); // 신규 DAO 메소드 호출
        }

        String order = "desc".equalsIgnoreCase(sort) ? "DESC" : "ASC";
        return detaildao.selectAllOrderByStartDate(order);
    }
}
