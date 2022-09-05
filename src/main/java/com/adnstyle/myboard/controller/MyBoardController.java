package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.domain.PageHandle;
import com.adnstyle.myboard.model.service.MyBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

//@RestController restapi
@Controller//타임리프를위해
public class MyBoardController {
   @Autowired
    public MyBoardService myBoardService;

   /*
   전체 게시글 리스트 조회
   */
    /*
    페이지네이션 적용전

*/   @GetMapping("/main")
    public String myBoardList(Model model ,@RequestParam(defaultValue = "1") int page){

       System.out.println("page"+page);//페이지는 1이 들어옴
       int totalCnt = myBoardService.countAll();//총 게시글 수

       PageHandle ph = new PageHandle(totalCnt,page,10);
       int totalPage = ph.getTotalPage();
       int beginPage = ph.getBeginPage();
       System.out.println("totalPage"+totalPage);
       System.out.println("beginPage"+beginPage);
       int endPage=ph.getEndPage();
       System.out.println("endPage"+endPage);

       ArrayList<Integer> pageNavi = new ArrayList();
       for(int i = beginPage; i<=endPage; i++){
           pageNavi.add(i);
       }
       int pageSize=ph.getPageSize();
       ArrayList<MyBoard> myBoardList = myBoardService.selectList(page);
       model.addAttribute("myBoardList",myBoardList);
       model.addAttribute("pageNavi",pageNavi);
       System.out.println("myBoardList"+myBoardList);
       System.out.println("pageNavi"+pageNavi);
       model.addAttribute("pageSize",pageSize);
       model.addAttribute("page",page);
       return "listBoard";
   }

 /*   //페이지네이션 적용
   @GetMapping("/main")//페이지번호 받아서 시작페이지랑 끝페이지 나오게 기본값은1
   public String myBoardList(@RequestParam(defaultValue ="1") int page, Model model){

       int totalCnt = myBoardService.countAll();//총 게시글 수

       PageHandle ph = new PageHandle(totalCnt,page,10);
       int start = ph.getBeginPage();
       int end = ph.getEndPage();

       model.addAttribute("start",start);
       model.addAttribute("end",end);
    System.out.println("st"+start);
       System.out.println("ed"+end);
       ArrayList<MyBoard> myBoardList = myBoardService.pageList(page,start,end);
       model.addAttribute("myBoardList",myBoardList);
       return "listBoard";
   }
*/
   /*
   해당번호의 게시글 리스트 조회
   */
    @GetMapping("/selectContent")
    public String myBoardContent(Model model, long id){
        //Long id = Long.parseLong(number);
        ArrayList<MyBoard> myContent = myBoardService.selectContent(id);
        model.addAttribute("myContent",myContent);
        return "boardContent";
    }

    /*
    게시글 삭제 상태값 Y로 변경
    */
    @GetMapping("/deleteContent")
    public void myBoardContentDelete(String number){
        Long id = Long.parseLong(number);
        myBoardService.deleteContent(id);
    }

    /*
    게시글 작성하기
    */

    @GetMapping("/writeForm")
    public String Write(){
        return "boardContent";//작성화면
    }
    @PostMapping("/insertContent")
    public String myBoardInsertContent(MyBoard board){
        System.out.println("입력한값"+board);
        myBoardService.insertContent(board);//db에 입력
        return "redirect:/main";

    }
    /*
    게시글 수정하기
    */
   /* @PostMapping("/updateContent")
    public void myBoardUpdateContent(MyBoard board){


        myBoardService.updateContent(board);

    }*/

    //타임리프 연습
    @GetMapping("/hello")
    public void hello(Model model){
        model.addAttribute("a","hello 타임리프");
    }
}
