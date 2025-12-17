package com.human.dalligo.vo.events;

import lombok.Data;

@Data
public class LshApplyVO {
    private int applyId;
    private String userId;
    private int eventId;
    private String applyDate;
    private String status;
}


