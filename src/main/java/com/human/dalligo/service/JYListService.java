package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.JYListDAO;
import com.human.dalligo.vo.JYListVO;
import com.human.dalligo.vo.JYPostVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYListService {
	
	private final JYListDAO listdao;
	
	// 전체 게시글 조회
	public List<JYListVO> getListAll(String category, String search) {
		
		// 카테고리 선택 됐다면
		if(category != null && !category.isEmpty()) {
			List<JYListVO> listByCategory = listdao.getListByCategory(category);
			return listByCategory;
		} 
		
		// 검색어가 있다면
		if(search != null && !search.isEmpty()) {
			List<JYListVO> listBySearch = listdao.getListBySearch(search);
			return listBySearch;
		} 
		
		// 전체 리스트
		List<JYListVO> list = listdao.getListAll(category, search);
		return list;
	}
	
	// 게시글 하나 조회
	public JYPostVO getListById(int id) {
		JYPostVO postvo = listdao.getListById(id);
		return postvo;
	}
}

