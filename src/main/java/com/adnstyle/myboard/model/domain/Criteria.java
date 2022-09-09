package com.adnstyle.myboard.model.domain;

import lombok.Data;
import lombok.Getter;

@Data//Data어노테이션 쓰는걸 지양하라는데 구현하고 고치기 @Setter를 하지말라말하기도..
public class Criteria {
    private int page;//현재 페이지

    private int pageSize;//한페이지당 보일 게시물 수

    public Criteria(){
        this(1,10);//기본값 1,10 주입받는걸로
    }
    public Criteria(int page,int pageSize){
        this.page=page;
        this.pageSize=pageSize;
    }
}
