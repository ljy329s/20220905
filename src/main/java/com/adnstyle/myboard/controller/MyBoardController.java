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
       ArrayList<MyBoard> myBoardList = myBoardService.selectList(page);
       model.addAttribute("myBoardList",myBoardList);
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
