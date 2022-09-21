package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyAttach;
import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.domain.PageHandle;
import com.adnstyle.myboard.model.service.JyAttachService;
import com.adnstyle.myboard.model.service.MyBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyBoardController {

    private final MyBoardService myBoardService;
    private final JyAttachService jyAttachService;

    /**
     * 전체 게시글 리스트+페이징처리+검색처리
     */
    @GetMapping("/")
    public String myBoardList(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "type", defaultValue = "A") String type, @RequestParam(value = "search", defaultValue = "") String search) {
        Map searchMap = new HashMap();
        searchMap.put("type", type);
        searchMap.put("search", search);
        int totalCnt = myBoardService.countAll(searchMap);
        int pageSize = 10;
        int naviSize = 10;
        PageHandle ph = new PageHandle(totalCnt, page, pageSize, naviSize);

        //검색조건+페이징처리
        searchMap.put("offset", ((page - 1) * pageSize));
        searchMap.put("pageSize", pageSize);

        ArrayList<MyBoard> myBoardList = myBoardService.selectList(searchMap);//게시글리스트 조회용

        model.addAttribute("myBoardList", myBoardList);
        model.addAttribute("ph", ph);
        model.addAttribute("type", type);
        model.addAttribute("search", search);
        System.out.println("myBoardList" + myBoardList);

        return "listBoard";
    }

    /**
     * 게시글 상세조회
     */
    @GetMapping("/boardContent")
    public String myBoardContent(Model model, long id, @RequestParam("page") int page, @RequestParam("type") String type, @RequestParam("search") String search) {
        ArrayList<MyBoard> myContent = myBoardService.selectContent(id);//게시글 번호로 내용 불러오기
        ArrayList<JyAttach> attachList = jyAttachService.attachList(id);
        model.addAttribute("myContent", myContent);//게시글내용
        model.addAttribute("attachList", attachList);//첨부파일
        model.addAttribute("page", page);//페이지
        model.addAttribute("type", type);//검색타입
        model.addAttribute("search", search);//검색내용

        System.out.println("attachList는" + attachList);
        System.out.println("myBoardContent 컨트롤러 model은" + model);

        return "boardContent";
    }

    /**
     * 게시글 삭제(상태값 변경)
     * 첨부파일 완전삭제
     */
    @GetMapping("/deleteContent")
    public String myBoardContentDelete(long id) {
        myBoardService.deleteContent(id);//게시물 삭제 결과가 있다면

        return "redirect:/";
    }

    /**
     * 게시글 작성폼으로 이동
     */
    @GetMapping("/writeForm")
    public String Write() {
        log.debug(" 게시글 작성폼으로 이동");
        return "writeForm";
    }

    /**
     * 게시글 등록 + 첨부파일등록 || 답글 + 첨부파일 등록
     */
    @PostMapping("/insertContent")
    public String myBoardInsertContent(MultipartFile[] uploadFile, MyBoard board) {
        myBoardService.insertContent(board, uploadFile);

        return "redirect:/";
    }

    /**
     * 첨부파일 다운로드
     */
    @GetMapping(value = "/downloadFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(String uploadPath, String fileName) {
        Resource resource = new FileSystemResource(uploadPath + "\\" + fileName);//역슬래시를 한번 문자열으로 출력하려면 \\두번! (파일경로와 폴더 사이에 역슬래시 있어야함!!!)
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Disposition", "attachment; " + "fileName=" +
                    new String(resourceName.getBytes("UTF-8"),
                    "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    /**
     * 게시글 수정하기
     */
    @PostMapping("/updateContent")
    public String myBoardUpdateContent(MyBoard board, MultipartFile[] uploadFile, @RequestParam(value = "attBno", required = false) List<Long> attList) {// )HttpServletRequest request
        List attFileList = new ArrayList();

        if (attList != null) {
            for (Long attBno : attList) {
                attFileList.add(attBno);
            }
            jyAttachService.deleteOnlyAttach(attFileList);
        }
        myBoardService.updateContent(board, uploadFile);//게시글수정

        return "redirect:/";
    }

    /**
     * 답변달기 폼으로 이동
     */
    @GetMapping("/answerForm")
    public String writeForm(Long id, Model model){
        model.addAttribute("id",id);
        return "answerForm";
    }


    /**
     * 답변 상세조회
     */
    @GetMapping("/answerContent")
    public String answerContent(Model model, long id, @RequestParam("page") int page, @RequestParam("type") String type, @RequestParam("search") String search) {
        System.out.println("답변상세조회 컨트롤러");
        ArrayList<MyBoard> myAnswerContent = myBoardService.selectContent(id);//게시글 번호로 내용 불러오기
        ArrayList<JyAttach> attachList = jyAttachService.attachList(id);
        model.addAttribute("myAnswerContent", myAnswerContent);//게시글내용
        model.addAttribute("attachList", attachList);//첨부파일
        model.addAttribute("page", page);//페이지
        model.addAttribute("type", type);//검색타입
        model.addAttribute("search", search);//검색내용

        System.out.println("attachList는" + attachList);
        System.out.println("myBoardContent 컨트롤러 model은" + model);

        return "answerContent";
    }

}
