package com.human.dalligo.vo.community;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class JYDetailVO {
	private int id;
	private int userFk;
	private String userId;
	private String nickName;
	private String title;
	private String content;
	private int countLikes;
	private int countComments;
	private LocalDateTime inDate;
	private List<JYFileVO> fileList;
}
