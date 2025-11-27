package com.human.dalligo.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RRcardInfoVO {
	private Integer id;
	private String title;
	private String subtitle;
	private LocalDate date;
	private Integer price;
	private String location;
}
