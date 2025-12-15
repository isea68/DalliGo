package com.human.dalligo.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LshTripVO {
	private int tripId;
	private int eventId;
	private String userId;
	private String startCity;
	private String endCity;
	private java.math.BigDecimal distance;
	private int cost;
	private int currentPeople;
	private Timestamp tripDate;
	private java.time.LocalTime tripTime;
	private String status;
	private Timestamp createdAt;
}