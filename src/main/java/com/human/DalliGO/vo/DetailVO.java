package com.human.DalliGO.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetailVO {
	private int id;
	private String title;
	private String location;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String distance;
	private String weather;
	private String org;
	private String regURL;
	private String description;
	private int total; 
	private LocalDateTime inDate;
}
