package com.adnstyle.myboard.model.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

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
    private LocalDateTime userBirth;

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
