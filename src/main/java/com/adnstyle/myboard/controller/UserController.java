package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor


public class UserController {

    private final JyUserService jyUserService;

    /**
     * 회원가입화면으로 이동
     */
    @GetMapping("/newUser")
    public String newMember(){
        return "newUserForm";
    }

    /**
     * 회원 등록
     */
    @PostMapping("/insertNewUser")
    public String insertNewUser(JyUser jyUser){

        jyUserService.insertNewMember(jyUser);

        return "listBoard";//임시방편으로 게시글화면으로
    }
}
