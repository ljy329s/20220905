package com.adnstyle.myboard.model.domain;

import java.time.LocalDateTime;

public class JyFreeBoard {

    private Long id;

    private String title;

    private String content;

    private String delYn;

    private int viewCount;

    private String createBy;

    private LocalDateTime createdDate;

    private String modifyBy;

    private LocalDateTime modifiedDate;


}
//    create table jy_free_board
//        (
//                id            bigint auto_increment comment '식별자' primary key,
//                title         varchar(100)           null,
//        content       text                   null,
//        del_yn        varchar(1) default 'N' null,
//        view_count    int                    null,
//        created_by    varchar(100)           null,
//        created_date  datetime(6)            null,
//        modified_by   varchar(100)           null,
//        modified_date datetime(6)            null
//        );

