package com.human.dalligo.vo;

import java.util.Date;
import lombok.Data;

@Data
public class SSOrderVO {
    private int orderId;       // AUTO_INCREMENT PK
    private int goodsId;
    private String goodsName;
    private int quantity;
    private String buyer;
    private String address;
    private String phone;
    private Date payDate;      // DB에서 NOW()로 자동 입력
}