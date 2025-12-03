package com.human.dalligo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.dalligo.dao.UserDAO;
import com.human.dalligo.vo.UserVO;



@Service
public class SignUPService {
	
	@Autowired
	UserDAO userDAO;
	public void insert(UserVO uservo) {
		userDAO.insert(uservo);
	}
}
