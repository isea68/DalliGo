package com.human.dalligo.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LshEventVO {
	private int id;
	private String title;
	private String location;
	private java.time.LocalDateTime startDate;
	private java.time.LocalDateTime endDate;
	private String distance;
	private java.math.BigDecimal price;
	private String org;
	private String regUrl;
	private String description;
	private java.time.LocalDateTime inDate;
	private int total;

}