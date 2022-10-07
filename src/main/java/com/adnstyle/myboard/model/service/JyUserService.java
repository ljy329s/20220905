package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.repository.JyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JyUserService {

    private final JyUserRepository jyUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void insertNewUser(JyUser jyUser) {
        String pw = jyUser.getUserPw();
        jyUser.setUserPw(passwordEncoder.encode(pw));

        jyUserRepository.insertNewUser(jyUser);

    }

    public int checkId(String userId) {
        int no = jyUserRepository.checkId(userId);
        return no;
    }

    public int checkEmail(String userEmail) {
        int no = jyUserRepository.checkEmail(userEmail);
        return no;
    }

    public int loginUser(String userId, String userPw) {

        //먼저 비밀번호 가져오기
        String encPw = jyUserRepository.selectUser(userId);
        System.out.println("encPw" + encPw);
        if (encPw != null && encPw != "") {//값이 있다면
            boolean pwCheck = passwordEncoder.matches(userPw, encPw);
            System.out.println("pwCheck" + pwCheck);
            if (pwCheck) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }
}

