package com.human.dalligo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.human.dalligo.dao.JSUserDAO;
import com.human.dalligo.service.JSUserService;
import com.human.dalligo.vo.JSUserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor

public class JSUserController {

    private final JSUserService userService;

    private final JSUserDAO userDAO;

	@GetMapping("/signup")
	public String getSignup(Model model) {
		return "user/signup";
	}

	@PostMapping("/signup")
	public String postSignup(@ModelAttribute("user") JSUserVO user, Model model) {
		userService.insert(user);
		return "redirect:/"; // 가입 후 메인(혹은 로그인)으로 리다이렉트
	}

	// 아이디 중복 체크
	@GetMapping("/checkUserId")
	@ResponseBody
	public Map<String, Object> checkUserId(@RequestParam("userId") String userId) {
		boolean exists = userDAO.getUserByUserId(userId) != null;
		Map<String, Object> resp = new HashMap<>();
		resp.put("available", !exists);

		return resp;
	}

	// 닉네임 중복 체크 (아이디 체크와 동일 구조)
	@GetMapping("/checkNickName")
	@ResponseBody
	public Map<String, Object> checkNickname(@RequestParam("nickName") String nickname) {
		boolean exists = userDAO.getUserByNickName(nickname) != null;
		Map<String, Object> resp = new HashMap<>();
		resp.put("available", !exists);
		return resp;
	}
    
    @PostMapping("/login")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        JSUserVO user = userService.login(userId, password);
        if(user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "main";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화 → 로그아웃
        return "redirect:/";
    }
    @GetMapping("/updateForm")
    public String updateForm(HttpSession session, Model model) {
        JSUserVO user = (JSUserVO) session.getAttribute("loginUser");
        if(user==null) return "redirect:/login";
        model.addAttribute("user", user);
        return "user/updateForm";
    }

    @PostMapping("/update")
    public String update(JSUserVO user, HttpSession session) {
        userService.updateUser(user);
        // 세션 정보 갱신
        session.setAttribute("loginUser", userService.getUserByUserId(user.getUserId()));
        return "redirect:/mypage";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(HttpSession session) {
        JSUserVO user = (JSUserVO) session.getAttribute("loginUser");
        if(user != null) {
            userService.deleteUser(user.getUserId());
            session.invalidate();
            return "success";
        }
        return "fail";
    }
}
