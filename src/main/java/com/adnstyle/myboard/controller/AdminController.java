package com.adnstyle.myboard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자계정을 위한 컨트롤러
 */
@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping("/adminPage")
    public String adminPage() {
        System.out.println("adminPage");
        return "admin/adminPage";
    }
}
