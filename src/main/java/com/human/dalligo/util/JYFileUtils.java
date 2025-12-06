//package com.human.dalligo.util;
//
//import java.io.IOException;
//import java.nio.file.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//// 일반bean은 component annotation 적용해야 객체가 만들어짐
//@Component
//public class JYFileUtils {
//
//	// 기본 업로드 경로
//	private static final String BASE_UPLOAD_DIR = "C:\\upload1";
//	
//	// 허용할 확장자 (소문자), 대문자면 통과하지 못 함
//	private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
//			"jpg", "jpeg", "png", "gif", "pdf", "docx", "txt"
//	);
//	
//	// 최대 파일 크기 (10MB)
//	private static final long MAX_FILE_SIZE = 10 * 2024 * 1024;
//	
//    /**
//     * 파일 저장
//     */					    //파일 정보가 저장되어 있는 객체
//	public String saveFile(MultipartFile file) {
//		validateFile(file);
//		
//        try {
//            // 업로드 경로 생성 (없으면 자동 생성)
//        	// 실제 하드 디스크에 접근 할 수 있는 객체
//            Path uploadPath = Paths.get(BASE_UPLOAD_DIR);
//            // 위치에 디렉토리가 없으면 만들어라...
//            Files.createDirectories(uploadPath);
//
//            // 확장자 및 파일명 생성
//            String ext = getExtension(file.getOriginalFilename());
//            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            String uuid = UUID.randomUUID().toString();
//            String fileName = timestamp + "_" + uuid + "." + ext;
//
//            Path fullPath = uploadPath.resolve(fileName);
//
//            // 파일 저장
//            file.transferTo(fullPath.toFile());
//
//            // 전체 경로 리턴
////            return fullPath.toString().replace("\\", "/");
//            // 파일명만 리턴하도록 수정 함
//            return fileName;
//        } catch (IOException e) {
//            throw new RuntimeException("파일 저장 실패: " + e.getMessage(), e);
//        }
//    }
//	
//    /**
//     * 파일 유효성 검사 (확장자, 크기)
//     */
//    private void validateFile(MultipartFile file) {
//        if (file == null || file.isEmpty()) {
//            throw new IllegalArgumentException("빈 파일입니다.");
//        }
//
//        String ext = getExtension(file.getOriginalFilename());
//        if (!ALLOWED_EXTENSIONS.contains(ext.toLowerCase())) {
//            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다: " + ext);
//            //예외(에러) 발생 시켜라
//        }
//
//        if (file.getSize() > MAX_FILE_SIZE) {
//            throw new IllegalArgumentException("파일 크기가 10MB를 초과했습니다.");
//        }
//    }
//
//    /**
//     * 파일 확장자 추출
//     */
//    public String getExtension(String originalName) {
//        if (originalName == null) return "";
//        int idx = originalName.lastIndexOf(".");
//        return (idx > 0) ? originalName.substring(idx + 1).toLowerCase() : "";
//    }
//}
