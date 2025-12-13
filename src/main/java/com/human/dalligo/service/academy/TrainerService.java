package com.human.dalligo.service.academy;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.human.dalligo.dao.academy.TrainerDAO;
import com.human.dalligo.vo.academy.TrainerVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TrainerService {

	private final TrainerDAO trainerdao;
	
	//S3 객체 주입
	private final AmazonS3 amazonS3;
	
	//	버킷 이름 주입
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	
	public void insert(TrainerVO trainervo) {		
		trainerdao.insert(trainervo);
	}
		
	public String s3upload(MultipartFile photoFile) throws IOException{
		
		String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(photoFile.getSize());
		metadata.setContentType(photoFile.getContentType());

		// 업로드
		amazonS3.putObject(
				bucketName,
				fileName,
				photoFile.getInputStream(),
				metadata
		);

		// 업로드된 파일 URL 반환
		return amazonS3.getUrl(bucketName, fileName).toString();
	}
	
	
	public List<TrainerVO> selectList(){
		List<TrainerVO> trainervo = trainerdao.selectAll();	
		return  trainervo;		
	}	
	

	
	public TrainerVO selectForLogin( TrainerVO trainervo ) {
		TrainerVO tvo = trainerdao.selectForLogin(trainervo);
		if (tvo!=null && tvo.getPassword().equals(trainervo.getPassword())) {
			return tvo; //로고인 성공
		}else {
			return null ; //로그인 실패
		}			
	}
	
	public TrainerVO select(Integer id) {
		TrainerVO tvo = trainerdao.selectById(id);
		
		return tvo;
	}

	

}
