package com.human.dalligo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.human.dalligo.dao.JYPostDAO;
import com.human.dalligo.util.JYFileUtils;
import com.human.dalligo.vo.JYFileVO;
import com.human.dalligo.vo.JYPostVO;

import ch.qos.logback.core.util.FileUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYPostService {

    private final JYFileUtils fileUtils;
	
	private final JYPostDAO postdao;
	
	@Transactional // 작업이 취소되면 DB로 롤백
	public void insert(JYPostVO postvo, List<MultipartFile> files) {
		
		// 파일명 DB에 저장할 때 필요한 post_id 먼저 가져와서 세팅
		postdao.insert(postvo);
		int postId = postvo.getId();
		
		// 파일명을 DB에 저장
		if(files.size() != 0) {
			// files에 여러개의 첨부파일이 있음. 그것을 하나씩 가져오기
			// 하나씩 가져와서 디렉토리에 저정하고, FileVO로 DB에 저장
			for(MultipartFile file : files) {
				if(file.getOriginalFilename() != "") {
					String originalName = file.getOriginalFilename();
					String saveName = fileUtils.saveFile(file);
					
					JYFileVO filevo = new JYFileVO();
					filevo.setPostId(postId);
					filevo.setOriginalName(originalName);
					filevo.setSavedName(saveName);
					
					//DAO단에 insert 요청
					postdao.insertFiles(filevo);
					
				
				}
			}
		}
		
		
	}

}
