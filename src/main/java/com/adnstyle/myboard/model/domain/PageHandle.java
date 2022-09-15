package com.adnstyle.myboard.model.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.web.util.UriComponentsBuilder;

@Setter
@Getter
@Alias("pageHandle")
public class PageHandle {
    private int totalCnt;//총 게시물
    private int totalPage;//총 페이지수
    private int naviSize;//네비 사이즈
    private int beginPage;//시작페이지
    private int endPage;//마지막 페이지
    private boolean showPrev;//이전으로 가기
    private boolean showNext;//다음으로 가기
    private int pageSize;//한페이지당 보일 게시글 수
    private int page;//현재 페이지

    /*검색용*/
    private String type;
    private String search;
    

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
        this.totalPage=(int)(Math.ceil(this.totalCnt/(double)pageSize));


        /*
            현재 네비의 마지막페이지
            (올림(페이지/(double)네비사이즈))*네비사이즈
            ex)보는 페이지가 3페이지, 네비사이즈10일때
            3/10(페이지 사이즈)=0.3->(올림)1 -> 1*10(네비사이즈)
            13 페이지일때
            13/10=1.3->(올림)->2->(*네비사이즈)=20
         */
        this.endPage= (int)Math.ceil(page/(double)naviSize)*naviSize;
        if(this.endPage>=this.totalPage){//endPage가 총페이지보다 크다면 총 페이지가 endPage인걸로
            this.endPage=this.totalPage;
        }

//        현재 네비의 시작페이지
        /*
            시작 페이지 구하기
			공식 : 현재페이지 / 페이징의 개수 * 페이징의 개수 + 1;
			startPage = currentPage / pagingCount * pagingCount + 1;
			보정 해줘야함 ??? 현재페이지가 5일 때는 5 / 5 = 1 으로 문제가 생김  currentPage는 5인데 startPage가 6이 되어버림
			if(currentPage % pagingCount == 0) {
			     startPage = startPage - 5(페이징의 개수);
				 startPage -= pagingCount;
			}
        */

       this.beginPage =this.page/this.naviSize * this.naviSize + 1;
        if(this.page % this.naviSize == 0){
            this.beginPage -= this.naviSize;
        }
        //만약 끝페이지가 시작페이지보다 작거나 같다면 1로!.. 왜냐하믄..ㅠㅠㅠ 아까 시작1 끝페이지0나옴ㅠㅠ
        if(this.endPage<this.beginPage){
            this.endPage=1;
        }

            //이전 버튼이 보이려면 시작페이지가 1이랑 같지 않을때
        this.showPrev= this.beginPage>1;

        //다음 버튼이 보이려면 끝페이지가 총페이지랑 같지 않을때
        this.showNext = this.endPage<this.totalPage;


    }
    

}
