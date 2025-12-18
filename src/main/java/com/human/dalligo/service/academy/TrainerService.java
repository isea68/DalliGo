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
		
	//S3에 업로드한 파일의 URL을 문자열로 반화하는 메서드
	public String s3upload(MultipartFile photoFile) throws IOException{
		
		//원본파일명+현재시간붙여서 새로운 파일명 생성--> 파일명 중복방지
		String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();

		//S3에 전달할 파일의 메타데이터 객체 생성
		ObjectMetadata metadata = new ObjectMetadata();
		
		//업로드할 파일의 크기 설정
		metadata.setContentLength(photoFile.getSize());
		
		//파일의 타입(MIME 타입, 예: image/png) 설정
		metadata.setContentType(photoFile.getContentType());

		// S3에 업로드
		amazonS3.putObject(
				bucketName,       //버킷이름
				fileName,			//S3에 저장될 파일이름
				photoFile.getInputStream(), //업로드할 파일 데이터
				metadata   // 파일 정보 (메타데이터)
		);

		// 업로드된 파일에 접근가능한 URL을 문자열로 반환
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

	
	 // 1. 강사 존재 여부 체크
	public void registerTrainer(TrainerVO vo) {

	    // 1. 강사 존재 여부 체크
	    TrainerVO existTrainer =
	            trainerdao.selectByTrainerId(vo.getTrainerId());
	    if (existTrainer != null) {
	        throw new IllegalStateException("ALREADY_TRAINER");
	    }
	    // 2. 가입 처리
	    trainerdao.insert(vo);
	}

	
	

}
