package com.human.dalligo.vo;

import lombok.Data;

@Data
public class LshUserVO {
    private int id;
    private String userId;
    private String password;
    private String name;
    private String nickName;
    private String address;
    private String phone;
    private int isAdmin;
}

