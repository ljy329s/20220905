package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor


public class JyUserController {

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

        System.out.println("jyUser"+jyUser);

        jyUserService.insertNewUser(jyUser);

        return "redirect:/";//임시방편으로 게시글화면으로
    }

    /**
     * 아이디 중복체크
     */
    @PostMapping("/checkId")
    @ResponseBody
    public int checkId(@RequestParam(value = "userId") String userId){
       int no = jyUserService.checkId(userId);
        System.out.println("no"+no);
    return no;
    }

}

