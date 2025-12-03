package com.human.dalligo.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JYCommentVO {
	
	private int id;
	private int postId;
	private int userFk;
	private String userId;
	private String nickName;
	private String content;
	private LocalDateTime inDate;
}
