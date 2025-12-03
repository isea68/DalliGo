package com.human.dalligo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.JYListDAO;
import com.human.dalligo.vo.JYListVO;
import com.human.dalligo.vo.JYPageVO;
import com.human.dalligo.vo.JYPostVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYListService {
	
	private final JYListDAO listdao;

	public List<JYListVO> getListAll() {
		List<JYListVO> list = listdao.getListAll();
		return list;
	}

	public JYPostVO getListById(int id) {
		JYPostVO postvo = listdao.getListById(id);
		return postvo;
	}

	public List<JYListVO> getResultBySearch(String search) {
		List<JYListVO> list = listdao.getListAll();
		List<JYListVO> listBySearch = new ArrayList<>();
		
		for(JYListVO item : list) {
			String nickName = item.getNickName();
			String title = item.getTitle();
			String content = item.getContent();
			
			if((nickName !=null && nickName.contains(search) || 
					title != null && title.contains(search) || 
					content != null && content.contains(search))){
				listBySearch.add(item);
			}
		}
		return listBySearch;
	}
	
	// 첫 랜더링 전체 글 조회
	public List<JYListVO> getListByPage(int page, int pageSize) {
		int offset = (page - 1) * pageSize;
		return listdao.findListByPage(offset, pageSize);
	}
	
	// 첫 랜더링 게시글 총 개수
	public int getTotalCount() {
		return listdao.countList();
	}

	// 페이지 단위 카테고리 글 조회
	public List<JYListVO> getListByCategoryPage(String category, int page, int pageSize) {
		int offset = (page - 1) * pageSize;
		
		return listdao.findListByCategoryPage(category, offset, pageSize);
	}

	public int getTotalCountByCategory(String category) {
		return listdao.countByCategory(category);
	}
	
	public JYPageVO getPage(String category, int page, int pageSize) {
		List<JYListVO> list;
		int totalCount;
		
		// DB에서 해당 페이지 게시물 가져오기
		if(category == null || category.isEmpty()) {
			// 전체 게시물
			list = getListByPage(page, pageSize); // 카테고리 상관없이 10개씩 나오는 게시물
			totalCount = getTotalCount();
		}else {
			// 카테고리 게시물
			list = getListByCategoryPage(category, page, pageSize);
			totalCount = getTotalCountByCategory(category);
		}
		
		return new JYPageVO(page, pageSize, totalCount, list);
	}
}
