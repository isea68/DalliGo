package com.human.dalligo.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JYListVO {
	private int id;
	private String category;
	private String nickName;
	private int isAdmin;
	private String title;
	private String content;
	private int countLikes;
	private int countComments;
	private LocalDateTime inDate;
}
