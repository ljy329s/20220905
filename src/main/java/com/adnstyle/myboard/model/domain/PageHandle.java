package com.adnstyle.myboard.model.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageHandle {
    private int totalCnt;//총 게시물
    private int pageSize;//한페이지당 보일 게시글 수
    private int page;//현재 페이지
    private int totalPage;//총 페이지수
    private int naviSize;//네비 사이즈
    private int beginPage;//시작페이지
    private int endPage;//마지막 페이지
    private boolean showPrev;//이전으로 가기
    private boolean showNext;//다음으로 가기
    

    //총게시글수, 해당페이지, 페이지당 보일 게시글 수

    public PageHandle(int totalCnt, int page, int pageSize, int naviSize){
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;
        this.naviSize = naviSize;
        /*
            총페이지수
            전체게시글 수/(double)페이지 사이즈 하고 소수점 올림
         */
        this.totalPage=(int)(Math.ceil(totalCnt/(double)pageSize));


        /*
            현재 네비의 마지막페이지
            (올림(페이지/(double)네비사이즈))*네비사이즈
            ex)보는 페이지가 3페이지, 네비사이즈10일때
            3/10(페이지 사이즈)=0.3->(올림)1 -> 1*10(네비사이즈)
            13 페이지일때
            13/10=1.3->(올림)->2->(*네비사이즈)=20
         */
        this.endPage= (int)Math.ceil(page/(double)naviSize)*naviSize;
        if(this.endPage>this.totalPage){//endPage가 총페이지보다 크다면 총 페이지가 endPage인걸로
            this.endPage=this.totalPage;
        }

//        현재 네비의 시작페이지
        this.beginPage=this.endPage-(this.naviSize)+1;
        if(this.endPage>this.totalPage){//마지막페이지가 총 페이지보다 크다면(잘못된거니까)
           this.beginPage=this.totalPage-((this.totalPage%this.naviSize-1));//total%네비사이즈의 나머지를 뺀다.
        }


        //이전 버튼이 보이려면 시작페이지가 1이랑 같지 않을때
        this.showPrev= beginPage!=1;

        //다음 버튼이 보이려면 끝페이지가 총페이지랑 같지 않을때
        this.showNext = totalPage!=endPage;


    }
}
