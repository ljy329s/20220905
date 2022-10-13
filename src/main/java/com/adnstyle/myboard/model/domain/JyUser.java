package com.adnstyle.myboard.model.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Alias("jyUser")
@Data
public class JyUser {
    /**
     * 고객번호
     */
    private Long userNo;

    /**
     * 고객아이디
     */
    private String userId;

    /**
     * 고객비밀번호
     */
    private String userPw;

    /**
     * 고객이름
     */
    private String userName;

    /**
     * 고객휴대폰번호
     */
    private String userPhone;

    /**
     * 고객이메일
     */
    private String userEmail;

    /**
     * 고객생년월일
     */
    //input type date로 넘어온값은 string이라 date로 저장하려니 에러남
    //데이트타입포맷 꼭 해주장..  vo date, db date로 해줌
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date userBirth;

    /**
     * 탈퇴여부
     */
    private String delYn;

    /**
     * 권한
     */
    private String role;

    /**
     * 가입일자
     */
    private LocalDateTime regDate;

    /**
     * 탈퇴일자
     */
    private LocalDateTime endDate;
}
