package com.human.dalligo.vo;

import lombok.Data;

@Data
public class UserInfoVO {
	private Integer id;
	private String userId ;
	private String password;
	private String name;
	private String nickName;
	private String address;
	private String phone;
	private String isAdmin;

}
