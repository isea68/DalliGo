package com.human.dalligo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.dalligo.dao.UserDAO;
import com.human.dalligo.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUPService {

    @Autowired
    private UserDAO userDAO;

    // 회원가입
    public void insert(UserVO uservo) {
        userDAO.insert(uservo);
    }

    // 아이디 사용 가능한지 확인
    public boolean isUserIdAvailable(String userId) {
        if (userId == null || userId.trim().isEmpty()) return false;
        return !userDAO.existsByUserId(userId);
    }

    // 닉네임 사용 가능한지 확인
    public boolean isNickNameAvailable(String nickName) {
        if (nickName == null || nickName.trim().isEmpty()) return false;
        return !userDAO.existsByNickName(nickName);
    }
}
