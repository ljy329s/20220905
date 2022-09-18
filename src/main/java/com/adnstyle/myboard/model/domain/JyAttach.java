package com.adnstyle.myboard.model.domain;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@Alias("jyAttach")
public class JyAttach {
    private Long attBno;//첨부파일 대표번호
   
    private String uuid;//uuid가 포함된 파일이름(저장될 파일명)

    private String uploadPath;//저장경로

    private String originName;//원본 파일명

    private boolean fileType;//이미지 존재 여부

    private long bno;//게시글 번호
}
/*
    FILE_NO NUMBER,                         --파일 번호
    BNO NUMBER NOT NULL,                    --게시판 번호
    ORG_FILE_NAME VARCHAR2(260) NOT NULL,   --원본 파일 이름
    STORED_FILE_NAME VARCHAR2(36) NOT NULL, --변경된 파일 이름
    FILE_SIZE NUMBER,                       --파일 크기
    REGDATE DATE DEFAULT SYSDATE NOT NULL,  --파일등록일
    DEL_GB VARCHAR2(1) DEFAULT 'N' NOT NULL,--삭제구분
    PRIMARY KEY(FILE_NO)                    --기본키 FILE_NO
*/