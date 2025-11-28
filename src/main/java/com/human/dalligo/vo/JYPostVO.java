package com.human.dalligo.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class JYPostVO {
	private int id;
	private String category;
	private String title;
	private int userId;
	private String content;
	private LocalDateTime inDate;
	private List<FileVO> fileList;
}
