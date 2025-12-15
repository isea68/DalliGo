package com.human.dalligo.vo;

import java.sql.Timestamp;

import com.human.dalligo.service.LshTripService;

import lombok.Data;

@Data
public class LshApplyListVO {

    private int applyId;
    private String userId;
    private String startCity;
    private String endCity;
    private String title;
    private Timestamp date;
    private int applyCount;
    private String status;

}
