package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.model.domain.JyAttach;
import com.adnstyle.myboard.model.domain.JyBoard;
import com.adnstyle.myboard.model.domain.JyReply;
import com.adnstyle.myboard.model.domain.PageHandle;
import com.adnstyle.myboard.model.service.JyAttachService;
import com.adnstyle.myboard.model.service.JyBoardService;
import com.adnstyle.myboard.model.service.JyReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final Logger logger = LoggerFactory.getLogger(JyBoardController.class);

    /**
     * 사이트메인화면(로그인후 뜰 화면)
     */
    @GetMapping("/jyHome")
    public String jyHome(){
        return "jyHome";
    }

    /**
     * 게시글 리스트(자유,답변)+페이징처리+검색처리(자유게시판)
     */
    @GetMapping("/boardList")
    public String qnaBoardList(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "type", defaultValue = "A") String type, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam("boardType") String boardType){
        Map searchMap = new HashMap();
        searchMap.put("type", type);
        searchMap.put("search", search);
        searchMap.put("boardType", boardType);
        int totalCnt = jyBoardService.countAll(searchMap);

        PageHandle ph = new PageHandle(totalCnt, page);

        searchMap.put("offset", ((page - 1) * ph.getPageSize()));
        searchMap.put("pageSize",ph.getPageSize());

        List<JyBoard> myBoardList = jyBoardService.selectList(searchMap);//게시글리스트 조회용

        model.addAttribute("myBoardList", myBoardList);
        model.addAttribute("ph", ph);
        model.addAttribute("type", type);
        model.addAttribute("search", search);

        if(boardType.equals("QnA_Board")){
            return "qnaBoardList";
        } else if (boardType.equals("Free_Board")) {
            return "freeBoardList";
        }
        return null;
    }


    /**
     * 게시글 상세조회
     */
    @GetMapping("/boardContent")
    public String myBoardContent(Model model, long id, @RequestParam("page") int page, @RequestParam("type") String type, @RequestParam("search") String search) {
        List<JyBoard> myContent = jyBoardService.selectContent(id);//게시글 번호로 내용 불러오기
        List<JyAttach> attachList = jyAttachService.attachList(id);//첨부파일리스트 조회하기

        model.addAttribute("myContent", myContent);//게시글내용
        model.addAttribute("attachList", attachList);//첨부파일

        model.addAttribute("page", page);//페이지
        model.addAttribute("type", type);//검색타입
        model.addAttribute("search", search);//검색내용
        return "freeBoardContent";

    }

    /**
     * 게시글 삭제(상태값 변경)
     * 첨부파일 완전삭제
     */
    @GetMapping("/deleteContent")
    public String myBoardContentDelete(long id, String boardType) {
        jyBoardService.deleteContent(id);//게시물 삭제 결과가 있다면

        if (boardType.equals("Free_Board")) {
            return "redirect:/user/boardList?boardType=Free_Board";
        } else if (boardType.equals("QnA_Board")) {
            return "redirect:/user/boardList?boardType=QnA_Board";
        }
        return null;
    }

    /**
     * 자유게시글 작성폼으로 이동
     */
    @GetMapping("/freeBoardForm")
    public String freeBoardForm() {
        return "freeBoardForm";
    }

    /**
     * 문의글 작성폼으로 이동
     */
    @GetMapping("/questionsForm")
    public String WriteForm() {
        return "questionForm";
    }

    /**
     * 게시글(질문글,자유게시판) 등록 + 첨부파일등록
     */
    @PostMapping("/insertContent")
    public String myBoardInsertContent(MultipartFile[] uploadFile, JyBoard board) {
        jyBoardService.insertContent(board, uploadFile);
        String boardType = board.getBoardType();
        if (boardType.equals("Free_Board")) {
            return "redirect:/user/boardList?boardType=Free_Board";
        } else if (boardType.equals("QnA_Board")) {
            return "redirect:/user/boardList?boardType=QnA_Board";
        }
        return null;
    }


    /**
     * 첨부파일 다운로드
     */
    @GetMapping(value = "/downloadFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    @ResponseBody
    /*
    ResponseEntity는 View를 제공하지 않는 형태로 요청을 처리하고, 직접 결과 데이터 및 http상태코드를 설정하여 응답할수있다.
    ResponseEntity를 사용하기 위해서는 응답상태코드 , 응답헤더,응답 봄문을 설정해줘야함
    */
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
    public String myBoardUpdateContent(JyBoard board, MultipartFile[] uploadFile, @RequestParam(value = "attBno", required = false) List<Long> attList, @RequestParam("boardType") String boardType,
                                       @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "type", defaultValue = "A") String type, @RequestParam(value = "search", defaultValue = "") String search, RedirectAttributes rttr) {// )HttpServletRequest request
        List attFileList = new ArrayList();
        System.out.println("attList"+attList);
        if (attList != null) {
            for (Long attBno : attList) {
                attFileList.add(attBno);
            }
            //jyAttachService.deleteOnlyAttach(attFileList);//첨부파일만 완전삭제
            jyAttachService.delAttachYn(attFileList);//첨부파일 상태값 변경
        }
        jyBoardService.updateContent(board, uploadFile);//게시글수정

        if(boardType.equals("QnA_Board")){
            rttr.addAttribute("page",page);
            rttr.addAttribute("boardType",boardType);
        }else if(boardType.equals("Free_Board")){
            rttr.addAttribute("page",page);
            rttr.addAttribute("search",search);
            rttr.addAttribute("type",type);
            rttr.addAttribute("boardType",boardType);

        }
        return "redirect:/user/boardList";
    }

    /**
     * 답변달기 폼으로 이동
     */
    @GetMapping("/answerForm")
    public String writeForm(Long id, Model model) {
        model.addAttribute("id", id);
        return "answerForm";
    }

    /**
     * 답변등록
     */
    @PostMapping("/insertAnswer")
    public String insertAnswer(MultipartFile[] uploadFile, JyBoard board) {
        jyBoardService.insertContent(board, uploadFile);
        return "redirect:/user/boardList?boardType=QnA_Board";
    }

    /**
    * 답변글 상세조회
    */
        @GetMapping("/answerContent")
        public String answerContent(Model model,long id, @RequestParam("page") int page){
            System.out.println("답변상세조회 컨트롤러");
            logger.debug("답 상세조회 컨트롤러=========================================");
            List<JyBoard> myAnswerContent = jyBoardService.selectContent(id);//게시글 번호로 내용 불러오기
            List<JyAttach> attachList = jyAttachService.attachList(id);
            model.addAttribute("myAnswerContent", myAnswerContent);//게시글내용
            model.addAttribute("attachList", attachList);//첨부파일
            model.addAttribute("page", page);//페이지
            return "answerContent";
        }

    /**
     * 문의글 상세조회
     */
    @GetMapping("/qnaContent")
    public String qnaContent(Model model,long id, @RequestParam("page") int page){
        List<JyBoard> myQnAContent = jyBoardService.selectContent(id);//게시글 번호로 내용 불러오기
        List<JyAttach> attachList = jyAttachService.attachList(id);
        model.addAttribute("myQnAContent", myQnAContent);//게시글내용
        model.addAttribute("attachList", attachList);//첨부파일
        model.addAttribute("page", page);//페이지
        return "questionContent";
    }

    /**
     * 답변삭제
     */

    @GetMapping("/deleteAnswer")
    public String deleteAnswer(Long id){
        jyBoardService.deleteAnswer(id);
        return "redirect:/user/boardList?boardType=QnA_Board";
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
     * 대댓글작성
     */

    @PostMapping("/insertChildReply")
    @ResponseBody
    public Map insertChildReply(@RequestBody JyReply childReply){
        Map<String,String> map = new HashMap<>();
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


