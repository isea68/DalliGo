package com.human.DalliGO.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.DalliGO.dao.DetailDAO;
import com.human.DalliGO.vo.DetailVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {
	@Autowired
	DetailDAO detaildao;
	public List<DetailVO> selectAll(){
		List<DetailVO> boards = detaildao.selectAll();
		return boards;
	}
}
