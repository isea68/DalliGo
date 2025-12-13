package com.human.dalligo.service.academy;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.human.dalligo.dao.academy.CourseDAO;
import com.human.dalligo.vo.academy.CourseVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

private final CourseDAO coursedao;
	
	//S3 객체 주입
	private final AmazonS3 amazonS3;
	
	//	버킷 이름 주입
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	
	
	public void insert(CourseVO coursevo) {
		coursedao.insert(coursevo);
		
	}
	
	//S3 업로드
	
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
	
	
	//전체조회
	            
	public List<CourseVO> selectList() {
		List<CourseVO> courses=coursedao.selectAll();	
		return  courses;
		
	}
	
	public CourseVO selectById(Integer id) {
		CourseVO course = coursedao.selectById(id);
		System.out.println("DB token="+course.getDeleteToken());

		return course;
		}
	


	//강습과정 삭제 서비스  : 강좌번호와 토큰
	public void deleteByToken(Integer courseId, String token) {
		System.out.println("=== deleteByToken START ===");
		
		//디버깅
		System.out.println("입력받은 id = " + courseId);
	    System.out.println("입력받은 token = " + token);
		
		//강좌 존재유무확인
		CourseVO course = coursedao.selectById(courseId);
		System.out.println("삭제할 강좌번호:"+ course);	
		
		if (course==null) {
			System.out.println("강좌가 존재하지 않음.");
			throw new RuntimeException("강좌가 존재하지 않습니다.");
		}	


		//token 확인
		if(!course.getDeleteToken().equals(token)) {
			System.out.println("Token mismatch!");
			throw new RuntimeException("권한이 없습니다.");
		}
		
	
		// S3 이미지 삭제
		String imageUrl =course.getPrphotoNew();
		if(imageUrl!=null &&imageUrl.contains(bucketName)) {
			
		//	System.out.println("prPhotoNew_url:"+imageUrl);
			//url 에서  key(파일명) 추출
		String key = imageUrl.substring(imageUrl.lastIndexOf("/")+1);	
		//System.out.println("url추출결과:"+key);	
		try {
		amazonS3.deleteObject(bucketName, key);
		//System.out.println("S3 이미지 삭제성공");
		}catch(Exception e) {
			 System.out.println("S3 삭제 에러:"+e.getMessage());
			    e.printStackTrace();
		}
			
		// 삭제 행이 나오는지 체크
		int rr = coursedao.deleteint(courseId);
		System.out.println("course del result:"+rr);
		
		if(rr==0) {
			System.out.println("DB 삭제 실패");
			throw new RuntimeException("삭제실패");
		}
		System.out.println("삭제완료");
				
	}
	
	}

	public List<CourseVO> findByTrainerId(Integer trainerPk) {		
		return coursedao.findByTrainerId(trainerPk);
	}	

}
