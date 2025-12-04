package com.human.dalligo.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JYLikeVO {
	private int id;
	private int postId;
	private String userId;
	private LocalDateTime inDate;
	
}
