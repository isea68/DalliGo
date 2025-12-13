package com.human.dalligo.vo.community;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class JYPostVO {
	private int id;
	private String category;
	private String title;
	private int userFk;
	private String content;
	private LocalDateTime inDate;
	private List<JYFileVO> fileList;
}
