package com.human.dalligo.vo;

import lombok.Data;

@Data
public class RRtrainerVO {

 private Integer id;
 private String trainerId;
 private String password;
 private String name ;
 private String photoOri; //file original name
 private String photoNew; // file changed name
 private String history;
	
 
}
