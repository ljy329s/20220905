package com.adnstyle.myboard.model.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageHandle {
    private int totalCnt;//총 게시물
    private int pageSize=10;//한페이지당 보일 게시글 수
    private int page;//현재 페이지
    private int totalPage;//총 페이지수
    private int naviSize;//네비 사이즈
    private int beginPage;//시작페이지
    private int endPage;//마지막 페이지
    private boolean showPrev;//이전으로 가기
    private boolean showNext;//다음으로 가기
    
     

    public PageHandle(int totalCnt, int page, int pageSize){
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;

        //총페이지수
        this.totalPage=(int)(Math.ceil(totalCnt/pageSize));

        //(올림처리((현재 페이지/10))*10
        //현재 네비의 끝페이지
        this.endPage= (int)(Math.ceil(this.page/10))*10;
        if(this.endPage>this.totalPage){
            this.endPage=this.totalPage;
        }
        //현재 네비의 시작페이지
        this.beginPage=this.endPage-9;
        if(endPage<10){//이거 꼭 적어줘야함ㅠㅠㅠㅠㅠㅠ
           this.beginPage=1;
        }

        //이전 버튼이 보이려면 시작페이지가 1이랑 같지 않을때
        this.showPrev= beginPage!=1;

        //다음 버튼이 보이려면 끝페이지가 총페이지랑 같지 않을때
        this.showNext = totalPage!=endPage;


    }
}
