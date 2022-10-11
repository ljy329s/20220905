package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.*;
import com.adnstyle.myboard.model.service.JyAttachService;
import com.adnstyle.myboard.model.service.JyBoardService;
import com.adnstyle.myboard.model.service.JyReplyService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")//로그인한 사람들만 접근할수있게
public class JyBoardController {

    private final JyBoardService jyBoardService;

    private final JyAttachService jyAttachService;

    private final JyReplyService jyReplyService;

    /**
     * 전체 게시글 리스트+페이징처리+검색처리
     */
    @GetMapping("/list")
    public String myBoardList(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "type", defaultValue = "A") String type, @RequestParam(value = "search", defaultValue = "") String search) {
        Map searchMap = new HashMap();
        searchMap.put("type", type);
        searchMap.put("search", search);
        int totalCnt = jyBoardService.countAll(searchMap);
        PageHandle ph = new PageHandle(totalCnt, page);

        searchMap.put("offset", ((page - 1) * ph.getPageSize()));
        searchMap.put("pageSize",ph.getPageSize());

        ArrayList<JyBoard> myBoardList = jyBoardService.selectList(searchMap);//게시글리스트 조회용

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
        ArrayList<JyBoard> myContent = jyBoardService.selectContent(id);//게시글 번호로 내용 불러오기
        ArrayList<JyAttach> attachList = jyAttachService.attachList(id);//첨부파일리스트 조회하기

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
     * 답변글 상세조회
     */
    @GetMapping("/qusetionContent")
    public String qnaBoardContent(Model model, long id, @RequestParam("page") int page, @RequestParam("type") String type, @RequestParam("search") String search) {
        Map<String, Object> contentBoardMap = jyBoardService.selectBoardContent(id);


        model.addAttribute("page", page);//페이지
        model.addAttribute("type", type);//검색타입
        model.addAttribute("search", search);//검색내용
        System.out.println("contentBoardMap"+contentBoardMap);
        model.addAttribute("contentBoardMap",contentBoardMap);

        return "questionContent";



    }

    /**
     * 게시글 삭제(상태값 변경)
     * 첨부파일 완전삭제
     */
    @GetMapping("/deleteContent")
    public String myBoardContentDelete(long id) {
        jyBoardService.deleteContent(id);//게시물 삭제 결과가 있다면

        return "redirect:/";
    }

//    /**
//     * 게시글 작성폼으로 이동
//     */
//    @GetMapping("/writeForm")
//    public String Write() {
//        log.debug(" 게시글 작성폼으로 이동");
//        return "writeForm";
//    }

    /**
     * 문의글 작성폼으로 이동
     */
    @GetMapping("/questionsForm")
    public String WriteForm() {
        log.debug(" 문의글 작성폼으로 이동");
        return "questionForm";
    }

    /**
     * 게시글(질문글,자유게시판) 등록 + 첨부파일등록
     */
    @PostMapping("/insertContent")
    public String myBoardInsertContent(MultipartFile[] uploadFile, JyBoard board) {
        jyBoardService.insertContent(board, uploadFile);
        String type = board.getBoardType();
        if (type.equals("QnA_Board")) {
            return "redirect:/user/qnaList";
        }else if(type.equals("Free_Board")) {
            return "redirect:/user/FreeBoard";
        }return null;
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
    public String myBoardUpdateContent(JyBoard board, MultipartFile[] uploadFile, @RequestParam(value = "attBno", required = false) List<Long> attList) {// )HttpServletRequest request
        List attFileList = new ArrayList();
        System.out.println("attlist"+attList);
        if (attList != null) {
            for (Long attBno : attList) {
                attFileList.add(attBno);
            }
            jyAttachService.deleteOnlyAttach(attFileList);//첨부파일삭제
        }
        jyBoardService.updateContent(board, uploadFile);//게시글수정

        return "redirect:/";
    }

    /**
     * 답변달기 폼으로 이동
     */
    @GetMapping("/answerForm")
    public String writeForm(Long id, Model model) {
        model.addAttribute("id", id);
        return "questionForm";
    }

    /**
     * 답변등록
     */
    @PostMapping("/insertAnswer")
    public String insertAnswer(MultipartFile[] uploadFile, JyBoard board) {
        jyBoardService.insertContent(board, uploadFile);{
            return "redirect:/";
        }
    }

    /**
    * 답변 상세조회
    */
        @GetMapping("/answerContent")
        public String answerContent(Model model,long id, @RequestParam("page") int page, @RequestParam("type") String type, @RequestParam("search") String search){
            System.out.println("답변상세조회 컨트롤러");
            ArrayList<JyBoard> myAnswerContent = jyBoardService.selectContent(id);//게시글 번호로 내용 불러오기
            ArrayList<JyAttach> attachList = jyAttachService.attachList(id);
            model.addAttribute("myAnswerContent", myAnswerContent);//게시글내용
            model.addAttribute("attachList", attachList);//첨부파일
            model.addAttribute("page", page);//페이지
            model.addAttribute("type", type);//검색타입
            model.addAttribute("search", search);//검색내용

            System.out.println("attachList는" + attachList);
            System.out.println("myBoardContent 컨트롤러 model은" + model);

            return "answer";
        }

    /**
     * 답변삭제
     */
    @GetMapping("/deleteAnswer")
    public String deleteAnswer(Long id){
        jyBoardService.deleteAnswer(id);

        return "redirect:/";

    }

    /**
     * 댓글작성
     */
    @PostMapping("/insertReply")
    @ResponseBody
    public Map<String,String> replySub(@RequestBody JyReply jyReply){
        Map <String,String> map = new HashMap<>();
       jyReplyService.insertReply(jyReply);
       map.put("result","success");
        return map;

    }

    /**
     * 하위댓글작성
     */

    @PostMapping("/insertChildReply")
    @ResponseBody
    public Map insertChildReply(@RequestBody JyReply childReply){
        Map<String,String> map = new HashMap<>();
        System.out.println("컨트롤러도착");
        System.out.println("ajax에서 넘어온값"+childReply);
        jyReplyService.insertChildReply(childReply);
        map.put("result","success");
        return map;
    }

    /**
     * 댓글삭제
     */
    @PostMapping("/deleteReply")
    @ResponseBody
    public Map deleteReply(@RequestParam(value = "delReBno") Long delReBno){
        Map <String,String> map = new HashMap<>();
        System.out.println(delReBno+"delReBno");
        jyReplyService.deleteReply(delReBno);
        map.put("result","success");
        return map;

    }

    /**
     * 댓글조회
     */
    @GetMapping("/selectReplyList")
    @ResponseBody
    public Map<String,Object> selectReplyList(@RequestParam(value = "boardBno") Long boardBno, @RequestParam(value = "page", defaultValue = "1") int page){

      return jyReplyService.selectReplyList(boardBno ,page);

    }
}


