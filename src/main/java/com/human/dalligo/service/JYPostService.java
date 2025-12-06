package com.human.dalligo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.human.dalligo.dao.JYPostDAO;
import com.human.dalligo.vo.JYFileVO;
import com.human.dalligo.vo.JYPostVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JYPostService {

	// S3 객체 주입
	private final AmazonS3 amazonS3;

	// 버킷 이름 주입
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	private final JYPostDAO postdao;

	@Transactional // 작업이 취소되면 DB로 롤백
	public void insert(JYPostVO postvo, List<MultipartFile> files) {

		// 1. 게시글 먼저 저장 -> 파일명 DB에 저장할 때 필요한 post_id 먼저 가져와서 세팅
		postdao.insert(postvo);
		int postId = postvo.getId();

		// 2. 파일 S3 업로드 + DB 저장
		if (files != null) {
			for (MultipartFile file : files) {

				if (!file.isEmpty()) {
					String originalName = file.getOriginalFilename();
					String savedName; //  수정: URL 저장
					try {
						savedName = s3upload(file); // s3에 업로드 후 URL 반환
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException("S3 업로드 실패"); // RuntimeException이 발생하면 자동 롤백
					}

					JYFileVO filevo = new JYFileVO();
					filevo.setPostId(postId);
					filevo.setOriginalName(originalName);
					filevo.setSavedName(savedName);

					postdao.insertFiles(filevo);
				}
			}
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

		// 업로드된 파일의 전체 URL 반환
		return amazonS3.getUrl(bucketName, savedName).toString();
	}

}
