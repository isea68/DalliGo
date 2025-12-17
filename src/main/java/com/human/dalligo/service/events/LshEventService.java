package com.human.dalligo.service.events;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.events.LshEventDAO;
import com.human.dalligo.vo.events.LshEventVO;

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

//	public int addEvent(LshEventVO vo) {
//		return eventDAO.insert(vo);
//	}
}

