package com.human.DalliGO.service;

import org.springframework.stereotype.Service;
import com.human.DalliGO.dao.UserDAO;
import com.human.DalliGO.vo.UserVO;

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
