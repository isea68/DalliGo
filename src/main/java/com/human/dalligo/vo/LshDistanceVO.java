package com.human.dalligo.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LshDistanceVO {
    private int id;
    private String startCity;
    private String endCity;
    private BigDecimal distance;
}

