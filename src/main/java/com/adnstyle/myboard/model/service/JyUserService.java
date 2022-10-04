//package com.adnstyle.myboard.model.service;
//
//import com.adnstyle.myboard.model.domain.JyUser;
//import com.adnstyle.myboard.model.repository.JyUserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class JyUserService {
//
//    private final JyUserRepository jyUserRepository;
//
//    @Transactional
//    public void insertNewUser(JyUser jyUser) {
//       BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//       String pw = jyUser.getUserPw();
//       String encPw = bCryptPasswordEncoder.encode(pw);
//       jyUser.setUserPw(encPw);
//
//      int no = jyUserRepository.insertNewUser(jyUser);
////      if(no>0){
////          //삭제성공
////      }else{
////          //삭제실패
////      }
//    }
//}
