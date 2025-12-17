package com.human.dalligo.vo.events;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LshTripSumVO {
    private String startCity;
    private String endCity;
    private String title;
    private Timestamp tripDate;
    private int totalPeople;
    private String status;
}

