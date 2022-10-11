package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor


public class JyUserController {

    private final JyUserService jyUserService;

    /**
     * 회원가입화면으로 이동
     */
    @GetMapping("/joinForm")
    public String newMember() {
        return "joinForm";
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public String insertNewUser(JyUser jyUser) {

        System.out.println("jyUser" + jyUser);


        jyUserService.insertNewUser(jyUser);

        return "/auth/loginForm";//회원가입 완료후 로그인화면으로 이동
    }

    /**
     * 아이디 중복체크(회원가입시)
     */
    @PostMapping("/checkId")
    @ResponseBody
    public int checkId(@RequestParam(value = "userId") String userId) {
        int no = jyUserService.checkId(userId);
        System.out.println("no" + no);
        return no;
    }

    /**
     * 이메일 중복체크(회원가입시)
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
    @GetMapping("/loginForm")
    public String loginForm() {
        return "/auth/loginForm";
    }

    //    @PostMapping("/login")
//    public String loginUser(@RequestParam (value="userId") String userId, @RequestParam(value="userPw") String userPw){
//        System.out.println("userId : "+userId);
//        System.out.println("userPw : "+userPw);
//        int no = jyUserService.loginUser(userId,userPw);
//        if(no==1) {//로그인 성공시 게시글 화면으로 이동
//            System.out.println("성공");
//            return "redirect:/";
//        }else{//로그인실패시 로그인화면으로 이동
//            System.out.println("실패");
//            return "loginForm";
//        }

    //시큐리티연습
    //@Secured 한개의 권한줄때 사용
    @Secured("ROLE_USER")//간단하게 권한걸수있다.
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보:";
    }

    //PreAuthorize메서드는 ROLE_ADMIN이런식으로 적을수 없고 hasRole('ROLE_MANAGER')이런식으로
    //hasRole부터 시작해서 적어줘야한다. 두개이상의 권한을 줄때 사용
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_MANAGER')")//data메소드가 실행되기 직전에 실행된다.
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보:";
    }
}
