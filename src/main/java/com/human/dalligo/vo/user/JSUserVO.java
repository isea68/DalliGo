package com.human.dalligo.vo.user;

import lombok.Data;

@Data
public class JSUserVO {
	private int id;
	private String userId;
	private String password;
	private String name;
	private String nickName;
	private String address;
	private String phone;
	private int isAdmin;
}
