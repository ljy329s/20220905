package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.service.MyBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MyBoardController {
   @Autowired
    public MyBoardService myBoardService;

   /*
   전체 게시글 리스트 조회
   */
   @GetMapping("/selectList")
    public ArrayList<MyBoard> myBoardList(){
       ArrayList<MyBoard> myBoardList = myBoardService.selectList();
       return myBoardList;
   }

   /*
   해당번호의 게시글 리스트 조회
   */
    @GetMapping("/selectContent")
    public ArrayList<MyBoard> myBoardContent(String number){
        Long id = Long.parseLong(number);
        ArrayList<MyBoard> myContent = myBoardService.selectContent(id);
        return myContent;
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
    @PostMapping("/insertContent")
    public void myBoardInsertContent(MyBoard board){
        myBoardService.insertContent(board);

    }
    /*
    게시글 수정하기
    */
    @PostMapping("/updateContent")
    public void myBoardUpdateContent(MyBoard board){
        myBoardService.updateContent(board);
    }
}
