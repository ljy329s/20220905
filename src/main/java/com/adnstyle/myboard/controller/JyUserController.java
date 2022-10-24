package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.auth.PrincipalDetails;
import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

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

    /**
     * 로그인 성공시이동할 화면 로그인을 성공하고 넘어가는 화면에서 세션을 생성!
     * 일반로그인, 소셜로그인 둘다! 부모클래스인 PrincipalDetails 사용했기때문
     */

    // @AuthenticationPrincipal 어노테이션을 사용하면 PrincipalDetails에서  return한 객체를 받아서 파라미터로 사용할수있다.
    @GetMapping("/user/userLogin")
    public String UserLogin(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpSession session) {
        JyUser jyUserSession  = principalDetails.getJyUser();
        session.setAttribute("jyUserSession",jyUserSession);
        return "jyHome";
    }

    /**
     * 로그인 실패시
     */
    @GetMapping("/failLogin")
    public String failLogin(){

    return "auth/failLoginForm";
    }

    /**
     * 아이디 비밀번호 찾기(아이디찾기)
     */
    @GetMapping("/findIdPw")
    public String findIdPw(){

        return "findIdPw";
    }
    /**
     * 입력정보와 일치하는 아이디가 있는지
     */
    @PostMapping("/findId")
    @ResponseBody
    public String findId(@RequestBody Map<String,String> jyUser){
        System.out.println("jyUser"+jyUser);
        String userId = jyUserService.findId(jyUser);
        System.out.println("userId"+userId);
        if(userId != null && userId != "" && userId.length()>0){
            return userId;
        }else{
            return null;
        }

    }


    /**
     * 비밀번호 찾기
     */
    @GetMapping("/findPw")
    public String findPw(){

        return "findPw";
    }



    /**
     * 마이페이지 화면으로 이동
     */
    @GetMapping("/myPage")
    public String myPage() {
        return "myPage";
    }


    //시큐리티연습
    //@Secured 한개의 권한줄때 사용
    @Secured("ROLE_USER")//간단하게 권한걸수있다.
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보:";
    }

    //PreAuthorize메서드는 ROLE_ADMIN이런식으로 적을수 없고 hasRole('ROLE_MANAGER')이런식으로
    //hasRole부터 시작해서 적어줘야한다. 두개이상의 권한을 줄때 사용
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_MANAGER')")//data메소드가 실행되기 직전에 실행된다.
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보:";
    }

}
