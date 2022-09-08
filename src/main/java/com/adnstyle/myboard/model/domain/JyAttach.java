package com.adnstyle.myboard.model.domain;

import lombok.Data;

@Data
public class JyAttach {
    private String uuid;//uuid가 포함된 파일이름

    private String uploadPath;//저장경로

    private String originName;//원본명

    private boolean fileType;//이미지 존재 여부

    private long bno;//게시글 번호
}
