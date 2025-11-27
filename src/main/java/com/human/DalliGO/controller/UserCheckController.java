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

    @GetMapping("/checkUserId")
    public Map<String,Object> checkUserId(@RequestParam("userId") String userId) {
        System.out.println("[DEBUG] /checkUserId called with userId=" + userId);
        boolean exists = userDAO.existsByUserId(userId);
        System.out.println("[DEBUG] exists = " + exists);
        Map<String,Object> resp = new HashMap<>();
        resp.put("available", !exists);
        return resp;
    }
}
