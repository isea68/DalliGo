package com.human.dalligo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.human.dalligo.dao.RRcourseDAO;
import com.human.dalligo.vo.RRcourseVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RRcourseService {
	
	private final RRcourseDAO coursedao;
	
	//S3 객체 주입
	private final AmazonS3 amazonS3;
	
	//	버킷 이름 주입
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	
	
	public void insert(RRcourseVO coursevo) {
		coursedao.insert(coursevo);
		
	}
	
	
	
	public String s3upload(MultipartFile prphotoFile) throws IOException{
		
		String fileName = System.currentTimeMillis() + "_" + prphotoFile.getOriginalFilename();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(prphotoFile.getSize());
		metadata.setContentType(prphotoFile.getContentType());

		// 업로드
		amazonS3.putObject(
				bucketName,
				fileName,
				prphotoFile.getInputStream(),
				metadata
		);

		// 업로드된 파일 URL 반환
		return amazonS3.getUrl(bucketName, fileName).toString();
	}
	
	
	
	            
	public List<RRcourseVO> selectList() {
		List<RRcourseVO> courses=coursedao.selectAll();	
		return  courses;
		
	}
	
	public RRcourseVO selectById(Integer id) {
		RRcourseVO course = coursedao.selectById(id);
		return course;
	}

}
