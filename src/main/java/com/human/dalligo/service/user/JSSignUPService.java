package com.human.dalligo.service.user;

import org.springframework.stereotype.Service;

import com.human.dalligo.dao.user.JSUserDAO;
import com.human.dalligo.vo.user.JSUserVO;

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
