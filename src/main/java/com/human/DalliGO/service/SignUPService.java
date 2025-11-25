package com.human.DalliGO.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.DalliGO.dao.UserDAO;
import com.human.DalliGO.vo.UserVO;

@Service
public class SignUPService {
	
	@Autowired
	UserDAO userDAO;
	public void insert(UserVO uservo) {
		userDAO.insert(uservo);
	}
}
