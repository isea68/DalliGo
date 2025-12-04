package com.human.dalligo.service;

import org.springframework.stereotype.Service;
import com.human.dalligo.dao.JSUserDAO;
import com.human.dalligo.vo.JSUserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JSSignUPService {

    private final JSUserDAO userDAO;

    // 회원가입
    public void insert(JSUserVO uservo) {
        userDAO.insert(uservo);
    }


}
