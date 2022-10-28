package com.adnstyle.myboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JyHomeController {

    /**
     * 메인화면이자 로그인 성공시 기본경로
     */
@GetMapping("/")
    public String jyHome(){
    return "jyHome";
    }
}
