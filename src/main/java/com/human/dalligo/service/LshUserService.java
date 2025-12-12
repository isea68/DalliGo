package com.human.dalligo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.human.dalligo.dao.LshUserDAO;
import com.human.dalligo.vo.LshUserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LshUserService {

    private final LshUserDAO userDAO;

    @Transactional(readOnly = true)
    public LshUserVO getUserByUserId(String userId) {
        return userDAO.selectUserByUserId(userId);
    }

    @Transactional(readOnly = true)
    public LshUserVO getUserById(int id) {
        return userDAO.selectUserById(id);
    }
}

