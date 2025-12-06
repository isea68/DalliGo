package com.human.dalligo.vo;

import lombok.Data;

@Data
public class RRtrainerVO {

 private Integer id;
 private String trainerId;
 private String password;
 private String name ;
 private String photoUrl; //dB에 저장할 사진경로
 private String history;
	
 
}
