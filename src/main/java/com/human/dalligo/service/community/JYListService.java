package com.human.dalligo.service.community;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.community.JYListDAO;
import com.human.dalligo.util.JYCategoryUtil;
import com.human.dalligo.vo.community.JYListVO;
import com.human.dalligo.vo.community.JYPostVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYListService {
	
	private final JYListDAO listdao;
	private final JYCategoryUtil categoryUtil; // 영어 category를 한글 category로 바꿔주는 util bean
	
	// 전체 게시글 조회
	public List<JYListVO> getListAll(String category, String search) {
		
		List<JYListVO> list;
		
		// 카테고리 선택 됐다면
		if(category != null && !category.isEmpty()) {
			list = listdao.getListByCategory(category);
		
		// 검색어가 있다면
		} else if(search != null && !search.isEmpty()) {
			list = listdao.getListBySearch(search);
		
		// 전체 리스트
		} else {
			list = listdao.getListAll(category, search);
		}
		
		// 공통 영어 카테고리 -> 한글 카테고리로 변환 처리
		list.forEach(vo -> vo.setCategory(categoryUtil.toKorean(vo.getCategory())));

		return list;
	}
	
	// 게시글 하나 조회
	public JYPostVO getListById(int id) {
		JYPostVO postvo = listdao.getListById(id);
		// 영어 카테고리 -> 한글 카테고리로 변환 처리
		postvo.setCategory(categoryUtil.toKorean(postvo.getCategory()));
		return postvo;
	}
}

