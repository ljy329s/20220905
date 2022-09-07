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
import java.util.HashMap;
import java.util.Map;

//@RestController restapi
@Controller//타임리프를위해
public class MyBoardController {
   @Autowired
    public MyBoardService myBoardService;

   /*
   전체 게시글 리스트 조회
   */


   @GetMapping("/main")
    public String myBoardList(Model model ,@RequestParam(defaultValue = "1") int page){
       int totalCnt=myBoardService.countAll();//총게시물의 수
       int pageSize = 10;
       int naviSize = 10;
       PageHandle ph = new PageHandle(totalCnt,page,pageSize,naviSize);//총게시물의 수,페이지사이즈, 네비사이즈

       //페이징처리
       Map pageMap = new HashMap();
       pageMap.put("offset",((page-1)*pageSize));//몇번부터 시작할건지
       pageMap.put("pageSize",pageSize);//화면에 몇개씩 보여줄건지
       ArrayList<MyBoard> myBoardList = myBoardService.selectList(pageMap);//게시글리스트 조회용
//       ArrayList<MyBoard> myBoardPage = myBoardService.myBoardPage(pageMap);//페이징처리 리스트 출력용
       model.addAttribute("myBoardList",myBoardList);
       model.addAttribute("ph",ph);

//
//       model.addAttribute("myBoardList",myBoardList);


       return "listBoard";
   }


   /*
   해당번호의 게시글 리스트 조회
   */
    @GetMapping("/boardContent")
    public String myBoardContent(Model model, long id){
        ArrayList<MyBoard> myContent = myBoardService.selectContent(id);
        model.addAttribute("myContent",myContent);
        return "boardContent";
    }

    /*
    게시글 삭제 상태값 Y로 변경
    */
    @GetMapping("/deleteContent")
    public String myBoardContentDelete(MyBoard board){
        myBoardService.deleteContent(board);
        return "redirect:/main";
    }

    /*
    게시글 작성하기
    */

    @GetMapping("/writeForm")
    public String Write(){
        return "writeForm";//작성화면
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
   @GetMapping("/updateContent")
   public String myBoardUpdateForm(Long id,Model model){
       ArrayList<MyBoard> myContent = myBoardService.selectContent(id);//글조회해서 띄우기
       model.addAttribute("myContent",myContent);
       return "updateBoardForm";

   }
   @PostMapping("/updateContent")
    public String myBoardUpdateContent(MyBoard board){
        myBoardService.updateContent(board);
        return "redirect:/main";

    }

}
