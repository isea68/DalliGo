package com.human.dalligo.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class JYDetailVO {
	private int id;
	private String nickName;
	private String title;
	private String content;
	private int countLikes;
	private int countComments;
	private LocalDateTime inDate;
	private List<FileVO> fileList;
}
