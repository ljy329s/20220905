package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.service.MyBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

//@RestController restapi
@Controller//타임리프를위해
public class MyBoardController {
   @Autowired
    public MyBoardService myBoardService;

   /*
   전체 게시글 리스트 조회
   */
   @GetMapping("/main")
    public String myBoardList(Model model){
       ArrayList<MyBoard> myBoardList = myBoardService.selectList();
       model.addAttribute("myBoardList",myBoardList);
       return "listBoard";
   }

   /*
   해당번호의 게시글 리스트 조회
   */
    @GetMapping("/selectContent")
    public String myBoardContent(Model model, long id){
        //Long id = Long.parseLong(number);
        ArrayList<MyBoard> myContent = myBoardService.selectContent(id);
        model.addAttribute("myContent",myContent);
        return "contentView";
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
        return "writeContent";//작성화면
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
