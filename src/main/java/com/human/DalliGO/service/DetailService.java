package com.human.DalliGO.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.DalliGO.dao.DetailDAO;
import com.human.DalliGO.vo.DetailVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {
    private final DetailDAO detaildao;

    // 기존 메소드 유지(필요하면)
    public List<DetailVO> selectAll(){
        return detaildao.selectAll();
    }

    // 정렬값("asc" 또는 "desc") 받아 DB에서 정렬해서 리턴
    public List<DetailVO> selectAllSortedByDate(String sort) {
        String order = "desc".equalsIgnoreCase(sort) ? "DESC" : "ASC";
        return detaildao.selectAllOrderByStartDate(order);
    }
}
