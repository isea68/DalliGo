package com.human.dalligo.service.community;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.human.dalligo.dao.community.JYDetailDAO;
import com.human.dalligo.dao.community.JYPostDAO;
import com.human.dalligo.vo.community.JYDetailVO;
import com.human.dalligo.vo.community.JYFileVO;
import com.human.dalligo.vo.community.JYPostVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYDetailService {

	private final JYDetailDAO detaildao;
	private final JYPostDAO postdao;
	private final AmazonS3 amazonS3;
	
	// 버킷 이름 주입
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	// DB에서 파일 정보 가져오기
	@Transactional(readOnly = true)
	public JYDetailVO detailById(int id) {
		JYDetailVO detail = detaildao.detailById(id);
		if(detail == null) {
			return null;
		}else {
			List<JYFileVO> files = detaildao.getFiles(id);
			detail.setFileList(files);
		}
		return detail;
	}
	
	// 단일 게시글 자세히 보기
	public JYPostVO getDetailByPostId(int postId) {
		JYPostVO detailvo = postdao.getDetailByPostId(postId);
		return detailvo;
	}

	// 단일 게시글 수정
	@Transactional // 중간에 하나라도 실패하면 rollback, 다 성공하면 한꺼번에 commit
	public void update(JYPostVO postvo, List<MultipartFile> files) {
		// 1. 기존 파일 목록 조회
		List<JYFileVO> oldFileList = detaildao.findFilesByPostId(postvo.getId());
		
		// 2. 기존 파일 삭제 처리 - S3 + DB 삭제
				if (oldFileList != null) {
					
					for(JYFileVO file : oldFileList) {
			
						String oldSavedUrl = file.getSavedName();

						// null 또는 빈값이면 삭제 처리 건너뛰기
						if(oldSavedUrl == null || oldSavedUrl.isBlank()) continue;
						
						// S3 파일 key만 가져와야 함(URL이므로)
						String key = extractObjectKeyFromUrl(oldSavedUrl);
						
						// S3에서 파일 삭제
						amazonS3.deleteObject(bucketName, key);		
					}
				
					// DB에서 파일 row 삭제
					detaildao.deleteFilesByPostId(postvo.getId());	
				}
		
		
		// 3. 새 파일 업로드 처리
		if (files != null && !files.isEmpty()) {
			
			// 3-1. 새 파일 업로드 + DB 저장
			for (MultipartFile file : files) {

				if (!file.isEmpty()) {
					String originalName = file.getOriginalFilename();
					String savedName; //  수정: URL 저장
					try {
						savedName = s3upload(file); // s3에 업로드 후 URL 반환 -> S3가 캐시 깨기용으로 URL뒤에 파라미터 붙임
						savedName = savedName.split("\\?")[0]; // 파라미터 제거 작업
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException("S3 업로드 실패"); // RuntimeException이 발생하면 자동 롤백
					}

					JYFileVO filevo = new JYFileVO();
					filevo.setPostId(postvo.getId());
					filevo.setOriginalName(originalName);
					filevo.setSavedName(savedName);

					postdao.insertFiles(filevo);
				}
			}
		}
		
		// 4. 게시글 내용 업데이트 - DB
		detaildao.update(postvo);
	}
	
	// 단일 게시글 삭제
	public void delete(Integer postId) {
		// 1. 기존 파일 목록 조회
		List<JYFileVO> oldFileList = detaildao.findFilesByPostId(postId);
		
		// 2. 기존 파일 삭제 처리 - S3 + DB 삭제
		if (oldFileList != null) {
			
			for(JYFileVO file : oldFileList) {
	
				String oldSavedUrl = file.getSavedName();

				// null 또는 빈값이면 삭제 처리 건너뛰기
				if(oldSavedUrl == null || oldSavedUrl.isBlank()) continue;
				
				// S3 파일 key만 가져와야 함(URL이므로)
				String key = extractObjectKeyFromUrl(oldSavedUrl);
				
				// S3에서 파일 삭제
				amazonS3.deleteObject(bucketName, key);	
			}
		
			// DB에서 파일 row 삭제
			detaildao.deleteFilesByPostId(postId);	
		}
		
		// 3. 게시글 내용 삭제 - DB
		detaildao.delete(postId);
	}
	
	// S3에서는 DB처럼 URL 전체 저장하는게 아니라 S3 Object Key로 파일 저장해놓기 때문에 Key값 따로 추출해야 함
	private String extractObjectKeyFromUrl(String oldSavedUrl) {
//		// 마지막 "/" 문자열이 key
//		String subStringUrl = oldSavedUrl.substring(oldSavedUrl.lastIndexOf("/") + 1);
//		return subStringUrl;
		
		try {
			// oldSavedUrl을 URL 객체로 변환 -> URL 객체 쓰면 URL 구성하는 부분(protocol, host, path)를 함수로 가져올 수 있음 
			URL url = new URL(oldSavedUrl);
			// 여기서 URL에서 getPath함수 써서 path만 가져올 수 있음
			String key = url.getPath().substring(1); // 문자열 맨 앞의 '/' 제거 = S3에서 사용할 key형태 맞춤(아직 URL 인코딩 상태)
			// URL 인코딩된 문자를 원래 문자로 반환 + UTF-8 문자셋 사용
			return URLDecoder.decode(key, StandardCharsets.UTF_8); // %EB%9F%B0%EB%A7%88%EB%A3%A82 -> 런마루2
		// URL 객체 생성 중 형식이 잘못되면 예외 발생(URL이 null이거나 형식이 잘못된 경우)
		} catch (MalformedURLException e) {
			// 예외 발생 시 콘솔에 오류 정보 출력(디버깅용)
			e.printStackTrace();
			return oldSavedUrl;
		}
	}

	// S3 업로드
	public String s3upload(MultipartFile file) throws IOException {

		String savedName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		// 파일 업로드
		amazonS3.putObject(bucketName, savedName, file.getInputStream(), metadata);

		// 업로드된 파일의 전체 URL 반환 + 캐시 방지 쿼리스트링 추가
	    String url = amazonS3.getUrl(bucketName, savedName).toString();
	    return url + "?v=" + System.currentTimeMillis();
	}
	
}
