package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 관리자계정을 위한 컨트롤러
 */
@Slf4j
@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class JyAdminController {

    private final JyAdminService jyAdminService;

    /**
     * 관리자 페이지로 이동시 고객 정보 조회하여 이동
     */
    @GetMapping("/adminPage")
    public String adminPage(Model model) {
        List<JyUser> jyUserList = new ArrayList();
        jyUserList = jyAdminService.selectUserList();
        model.addAttribute("jyUserList",jyUserList);
        return "adminPage";
    }
}
