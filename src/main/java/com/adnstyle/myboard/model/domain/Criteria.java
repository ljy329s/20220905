package com.adnstyle.myboard.model.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Criteria {//검색의 기준:pageNum과 amount를 전달하는 용도

    private int pageNum;//페이지 번호
    private int amount;//한페이지 당 보여줄 데이터

    public Criteria(){//아무 파라미터를 넣지않으면 기본값 한 페이지당 10개의 데이터
        this(1,10);
    }

    public Criteria(int pageNum, int amount){
        this.pageNum=pageNum;
        this.amount=amount;
    }
}
