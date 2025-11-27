package com.human.dalligo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.human.dalligo.dao.UserDAO;

@RestController
public class UserCheckController {

    private final UserDAO userDAO;

    public UserCheckController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // 아이디 중복 체크
    @GetMapping("/checkUserId")
    public Map<String,Object> checkUserId(@RequestParam("userId") String userId) {
        boolean exists = userDAO.getUserByUserId(userId) != null;        
        Map<String,Object> resp = new HashMap<>();
        resp.put("available", !exists);
        
        return resp;
    }

    // 닉네임 중복 체크 (아이디 체크와 동일 구조)
    @GetMapping("/checkNickName")
    public Map<String,Object> checkNickname(@RequestParam("nickName") String nickname) {
        boolean exists = userDAO.getUserByNickName(nickname) != null;        
        Map<String,Object> resp = new HashMap<>();
        resp.put("available", !exists);
        return resp;
    }
}
