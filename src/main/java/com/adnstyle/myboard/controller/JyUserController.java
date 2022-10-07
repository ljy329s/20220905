package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor


public class JyUserController {

    private final JyUserService jyUserService;

    /**
     * 회원가입화면으로 이동
     */
    @GetMapping("/newUser")
    public String newMember() {
        return "newUserForm";
    }

    /**
     * 회원 등록
     */
    @PostMapping("/insertNewUser")
    public String insertNewUser(JyUser jyUser) {

        System.out.println("jyUser" + jyUser);

        jyUserService.insertNewUser(jyUser);

        return "redirect:/";//임시방편으로 게시글화면으로
    }

    /**
     * 아이디 중복체크
     */
    @PostMapping("/checkId")
    @ResponseBody
    public int checkId(@RequestParam(value = "userId") String userId) {
        int no = jyUserService.checkId(userId);
        System.out.println("no" + no);
        return no;
    }

    /**
     * 이메일 중복체크
     */
    @PostMapping("/checkEmail")
    @ResponseBody
    public int checkEmail(@RequestParam(value = "userEmail") String userEmail) {
        int no = jyUserService.checkEmail(userEmail);
        System.out.println("no" + no);
        return no;

    }

    /**
     * 로그인화면으로 이동
     */
    @GetMapping("/login")
    public String loginForm(){
        return "/auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam (value="userId") String userId, @RequestParam(value="userPw") String userPw){
        System.out.println("userId : "+userId);
        System.out.println("userPw : "+userPw);
        int no = jyUserService.loginUser(userId,userPw);
        if(no==1) {//로그인 성공시 게시글 화면으로 이동
            System.out.println("성공");
            return "redirect:/";
        }else{//로그인실패시 로그인화면으로 이동
            System.out.println("실패");
            return "/auth/login";
        }
    }

}

