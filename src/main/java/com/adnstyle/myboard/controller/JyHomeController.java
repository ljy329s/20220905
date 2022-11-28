package com.adnstyle.myboard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class JyHomeController {

    /**
     * 메인화면이자 로그인 성공시 기본경로
     */
    @GetMapping("/")
    public String jyHome() {
        log.debug("메인화면 접속");
        return "jyHome";
    }
}
