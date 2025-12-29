package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.LshEventDAO;
import com.human.dalligo.vo.LshEventVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LshEventService {

	private final LshEventDAO eventDAO;

	public List<LshEventVO> getAllEvents() {
		return eventDAO.selectAll();
	}

	public LshEventVO getEvent(int id) {
		return eventDAO.selectOne(id);
	}

	public int getTotalEventCount() {
		return eventDAO.getTotalEventCount();
	}
	
	public int getTotalEventCount(String keyword) {
	    if (keyword == null || keyword.isBlank()) {
	        return eventDAO.getTotalEventCount();   // 기존 메소드 재사용
	    }
	    return eventDAO.getTotalEventCountWithSearch(keyword);
	}

	
	public List<LshEventVO> getEventListWithSearch(
	        int offset, int size, String keyword) {
	    return eventDAO.selectEvents(offset, size, keyword);
	}

	
	public List<LshEventVO> getEventList(int offset, int size) {
        return eventDAO.selectEventList(offset, size);
    }

//	public int addEvent(LshEventVO vo) {
//		return eventDAO.insert(vo);
//	}
}

