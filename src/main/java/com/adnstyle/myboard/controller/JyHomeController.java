package com.adnstyle.myboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JyHomeController {
@GetMapping("/")
    public String jyHome(){

    return "jyHome";
    }
}
