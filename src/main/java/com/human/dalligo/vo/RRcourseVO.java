package com.human.dalligo.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RRcourseVO {
	private Integer id;
	private Integer trainerId;
	private String title;
	private LocalDateTime startDate;
	private String selectDay;
	private String location;
	private String place;
	private String supportItem;
	private Integer price;
	private String duration;
	private String prphotoOri; // file original name
	private String prphotoNew; // file changed name
	private Integer capacity;
	private String eduType;
}
