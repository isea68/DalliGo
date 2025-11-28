package com.human.dalligo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.human.dalligo.service.UserService;
import com.human.dalligo.vo.UserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/updateForm")
    public String updateForm(HttpSession session, Model model) {
        UserVO user = (UserVO) session.getAttribute("loginUser");
        if(user==null) return "redirect:/login";
        model.addAttribute("user", user);
        return "updateForm";
    }

    @PostMapping("/update")
    public String update(UserVO user, HttpSession session) {
        userService.updateUser(user);
        // 세션 정보 갱신
        session.setAttribute("loginUser", userService.getUserByUserId(user.getUserId()));
        return "redirect:/mypage";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("loginUser");
        if(user != null) {
            userService.deleteUser(user.getUserId());
            session.invalidate();
            return "success";
        }
        return "fail";
    }
}
