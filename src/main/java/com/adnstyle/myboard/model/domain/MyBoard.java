package com.adnstyle.myboard.model.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.sql.Date;

@Alias("myBoard")
@Data
public class MyBoard {
    private Long id; //글번호
    private String title; //글제목
    private String content; //글내용
    private char delYn; //삭제여부 디폴트값('N')
    private int viewCount;// 조회수
    private Date createdDate;// 작성일
    private String createdBy;// 작성자
    private String modifiedBy;// 수정자
    private Date modifiedDate;// 수정일

}
