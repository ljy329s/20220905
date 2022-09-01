package com.adnstyle.myboard.model.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class MyBoard {
    private Long id; //글번호
    private String title; //글제목
    private String content; //글내용
    private char delYn; //삭제여부 디폴트값('N')
    private int viewCounter;// 조회수
    private Date createdDate;// 작성일
    private String createdBy;// 작성자
    private String modifideBy;// 수정자
    private Date modifideDate;// 수정일

}
