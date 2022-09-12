package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyAttach;
import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.domain.PageHandle;
import com.adnstyle.myboard.model.service.JyAttachService;
import com.adnstyle.myboard.model.service.MyBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

//@RestController restapi
@Controller//타임리프를위해
@RequiredArgsConstructor
public class MyBoardController {

    private final MyBoardService myBoardService;
    private final JyAttachService jyAttachService;

//    @GetMapping("/main")
//    public String myBoardList(Model model , @RequestParam(defaultValue = "1") int page,PageHandle pageHandle){
//       int totalCnt = myBoardService.countAll(pageHandle);//총게시물의 수
//       int pageSize = 8;
//       int naviSize = 5;
//
//       //보고있는 페이지의 시작페이지와 끝페이지 정보를 가져온다
//       PageHandle ph = new PageHandle(totalCnt,page,pageSize,naviSize);//총게시물의 수,페이지사이즈, 네비사이즈
//
//       //페이징처리
//       Map pageSearchMap = new HashMap();
//       pageSearchMap.put("offset",((page-1)*pageSize));//몇번부터 시작할건지
//       pageSearchMap.put("pageSize",pageSize);//화면에 몇개씩 보여줄건지
//       pageSearchMap.put("type",pageHandle.getType());
//       pageSearchMap.put("search",pageHandle.getSearch());
//
//
//       ArrayList<MyBoard> myBoardList = myBoardService.selectList(pageSearchMap);//게시글리스트 조회용
//
//       model.addAttribute("myBoardList",myBoardList);
//       model.addAttribute("ph",ph);
//
//       return "listBoard";
//   }
    @GetMapping("/main")
    public String myBoardList(Model model , @RequestParam(value="page", defaultValue = "1") int page,@RequestParam(value="type",defaultValue = "A") String type,@RequestParam(value = "search", defaultValue = "")String search){//required = false
        Map searchMap = new HashMap();
        searchMap.put("type",type);
        searchMap.put("search",search);
        int totalCnt = myBoardService.countAll(searchMap);//검색조건으로 찾은 총게시물의 수
        int pageSize = 5;
        int naviSize = 5;

        //보고있는 페이지의 시작페이지와 끝페이지 정보를 가져온다
        PageHandle ph = new PageHandle(totalCnt,page,pageSize,naviSize);//총게시물의 수,페이지사이즈, 네비사이즈

        //검색조건+페이징처리
        searchMap.put("offset",((page-1)*pageSize));//몇번부터 시작할건지
        searchMap.put("pageSize",pageSize);//화면에 몇개씩 보여줄건지

        ArrayList<MyBoard> myBoardList = myBoardService.selectList(searchMap);//게시글리스트 조회용

        model.addAttribute("myBoardList",myBoardList);//보여질 정보
        model.addAttribute("ph",ph);//화면에 보일 페이지네비
        System.out.println("myBoardList"+myBoardList);
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

//    @PostMapping("/insertContent")
//    public String myBoardInsertContent(MyBoard board){
//        System.out.println("입력한값"+board);
//        myBoardService.insertContent(board);//db에 입력
//        return "redirect:/main";
//
//    }
/*
첨부파일 포함 게시물 등록
*/
//년/월/일 폴더 생성 메서드
private String getFolder(){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//mm은 분 MM은 월!

    Date date = new Date();

    String str = sdf.format(date);//오늘날짜를 지정한 포멧 형식으로 변환
    
    //format패턴의 "-"를 os의 구분자로 바꾸겠다 os마다 구분자가 달라서 File.separator 적어줘야함
    return str.replace("-",File.separator);
    

}
    // 게시글 등록
    @PostMapping("/insertContent")
    public String myBoardInsertContent(MultipartFile[] uploadFile, MyBoard board, Model model){

        String uploadFolder = "C:\\upload"; //파일이 저장될 상위경로

        //같은폴더에 파일이 많으면 속도 저하 개수제한 문제등이 생긴다 날짜로 폴더 만들어주기
        File uploadPath = new File(uploadFolder, getFolder());//File(상위경로,하위경로?)

        if(uploadPath.exists()==false){
            uploadPath.mkdirs();//mkdirs(); 폴더 만드는 메서드
            System.out.println("폴더생성");
        }else{
            System.out.println("이미 폴더가 있습니다");
        }

        String originUploadFileName="";
        String changeUploadFileName="";
        for(MultipartFile multipartFile : uploadFile){

            originUploadFileName = multipartFile.getOriginalFilename();//파일원본명
            long size = multipartFile.getSize();//파일사이즈

            System.out.println("uploadFileName "+originUploadFileName + "size"+size);

            //동일한 파일명일때 기존파일 덮어버리는 문제 해결위해 UUID
            UUID uuid = UUID.randomUUID();
            changeUploadFileName = uuid.toString()+"-"+originUploadFileName;//랜덤uuid+"-"+원본명
            File saveFile = new File(uploadPath, originUploadFileName);

            try {
                multipartFile.transferTo(saveFile);//파일에 저장 try Catch해주기
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        System.out.println("입력한값"+board);
        myBoardService.insertContent(board);//db에 입력
        Long id = myBoardService.selectId();

        Map<String,Object> fileMap = new HashMap<>();
        fileMap.put("uuid",changeUploadFileName);
        fileMap.put("uploadPath",uploadPath);
        fileMap.put("originName",originUploadFileName);
        fileMap.put("bno",id);
        System.out.println("fileMap은?"+fileMap);

        jyAttachService.insertFile(fileMap);

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
