package com.human.dalligo.service;

import org.springframework.stereotype.Service;
import com.human.dalligo.dao.UserDAO;
import com.human.dalligo.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUPService {

    private final UserDAO userDAO;

    // 회원가입
    public void insert(UserVO uservo) {
        userDAO.insert(uservo);
    }


}
