package com.human.dalligo.vo;

import lombok.Data;

@Data
public class FileVO {
	private Integer id; //FileVO의 기본키
	private Integer postId; //해당글의 번호
	private String originalName; //실제 파일명
	private String savedName; //변경된 파일명
}
