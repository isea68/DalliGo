package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.human.dalligo.dao.JYDetailDAO;
import com.human.dalligo.dao.JYPostDAO;
import com.human.dalligo.vo.FileVO;
import com.human.dalligo.vo.JYDetailVO;
import com.human.dalligo.vo.JYPostVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYDetailService {

	private final JYDetailDAO detaildao;
	private final JYPostDAO postdao;

	@Transactional(readOnly = true)
	public JYDetailVO detailById(int id) {
		JYDetailVO detail = detaildao.detailById(id);
		if(detail == null) {
			return null;
		}else {
			List<FileVO> files = detaildao.getFiles(id);
			detail.setFileList(files);
		}
		return detail;
	}


	public JYPostVO getDetailById(int id) {
		JYPostVO detailvo = postdao.getDetailById(id);
		return detailvo;
	}


	public void update(JYPostVO postvo) {
		postdao.update(postvo);
	}
	
	public void delete(Integer id) {
		detaildao.delete(id);
		
	}
	
}
