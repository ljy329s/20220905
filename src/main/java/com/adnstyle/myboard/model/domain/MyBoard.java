package com.adnstyle.myboard.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Alias("myBoard")
@Data
@NoArgsConstructor
public class MyBoard {
    private Long id; //글번호
    private String title; //글제목
    private String content; //글내용
    private char delYn; //삭제여부 디폴트값('N')
    private int viewCount;// 조회수
    private LocalDateTime createdDate;// 작성일
    private String createdBy;// 작성자
    private String modifiedBy;// 수정자
    private LocalDateTime modifiedDate;// 수정일

    private List<JyAttach> jyAttachList;

}
