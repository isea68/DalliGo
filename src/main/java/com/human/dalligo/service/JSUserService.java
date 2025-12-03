package com.human.dalligo.service;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.JSUserDAO;
import com.human.dalligo.vo.JSUserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JSUserService {

	 private final JSUserDAO userDAO;

	    public void insert(JSUserVO user) {
	        userDAO.insert(user);
	    }

	    public JSUserVO login(String userId, String password) {
	        JSUserVO user = userDAO.getUserByUserId(userId);
	        if (user != null && user.getPassword().equals(password)) {
	            return user;
	        }
	        return null;
	    }

	    public JSUserVO getUserByUserId(String userId) {
	        return userDAO.getUserByUserId(userId);
	    }


	    public void updateUser(JSUserVO user) {
	        userDAO.updateUser(user);
	    }

	    public void deleteUser(String userId) {
	        userDAO.deleteUser(userId);
	    }
}
