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
        int no =jyUserRepository.checkId(userId);
        return no;
    }
}
