package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.repository.JyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JyUserService {

    private final JyUserRepository jyUserRepository;
    //private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void insertNewMember(JyUser jyUser) {
        jyUser.getUserPw();//비밀번호 암호화

    }
}
