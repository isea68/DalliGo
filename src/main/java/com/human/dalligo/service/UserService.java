package com.human.dalligo.service;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.UserDAO;
import com.human.dalligo.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public void insert(UserVO user) {
        userDAO.insert(user);
    }

    public UserVO login(String userId, String password) {
        UserVO user = userDAO.getUserByUserId(userId);
        if(user != null && user.getPassword().equals(password)) {        	
            return user;
        }
        return null;
    }
}
