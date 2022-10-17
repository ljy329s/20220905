package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.auth.PrincipalDetails;
import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
     */

    // @AuthenticationPrincipal 어노테이션을 사용하면 UserDetails에서  return한 객체를 받아서 파라미터로 사용할수있다.
    @GetMapping("/user/userLogin")
    public String UserLogin(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpSession session) {
        JyUser jyUserSession  = principalDetails.getJyUser();
        session.setAttribute("jyUserSession",jyUserSession);
        System.out.println(jyUserSession);
        return "jyHome";
    }

    /**
     * 마이페이지 화면으로 이동
     *
     * @return
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
//    @GetMapping("/test/login")
//    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){//DI 의존성주입 Authentication객체안에 Principal이 있고
//        //Principal의 리턴타입이 오브젝트이기 때문에 다운캐스팅으로 받아서 PrincipalDetails를
//        System.out.println("/test/login----------------");
//        PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal();
//        System.out.println("authentication"+principalDetails.getJyUser());
//        System.out.println("userDetails"+principalDetails.getJyUser());
//        return "세션정보확인하기";
//    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){//DI 의존성주입 Authentication객체안에 Principal이 있고
        System.out.println("/test/oauth/login----------------");
        OAuth2User oAuth2User =(OAuth2User) authentication.getPrincipal();

        System.out.println("authentication"+oAuth2User.getAttributes());
        System.out.println("oauth2User"+oauth.getAttributes());
        return "OAuth 세션정보 확인하기";
    }

    //정리

    /*
    스프링시큐리티 자기만의시큐리티세션을 가지고있다.
    서버자체가 가지고있는 세션영역이 있는데 그안에 시큐리티가 관리하는 세션영역이 있다.
    시큐리티가 관리하는 세션에 들어갈수있는 타입은
    Authentication객체 밖에 없다 그리고 얘를 꺼내올수있다
    컨트롤러에서 DI할수있다.
    Authentication객체안에 들어갈수있는 두개의 타입이 있는데
    UserDtails타입, OAuth2User타입
    즉 시큐리티가 들고있는 세션에는 Authentication객체만 넣을수있다 얘가 딱 들어가는 순간 로그인이됨
    일반적인 로그인을 할때 UserDtails타입으로 Authentication객체안에 들어가고,
    페이스북 로그인, 구글로그인 등을 할때 OAuth로그인을 하게되면 OAuth2User타입이 Authentication객체 안에 들어간다

    그럼 일반로그인과 소셜로그인을 둘다 허용하는경우에 컨트롤러에는 뭘 적어야할까?
    OAuth2User타입과 UserDtails타입을 implements 하는 부모 클래스를 두고
    그 부모 클래스를 Authentication안에 담아줌
    * */

}
