package com.adnstyle.myboard.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.adnstyle.myboard.model.domain.JyAttach;
import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.domain.PageHandle;
import com.adnstyle.myboard.model.service.JyAttachService;
import com.adnstyle.myboard.model.service.MyBoardService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller//타임리프를위해
@RequiredArgsConstructor
public class MyBoardController {

    private final MyBoardService myBoardService;
    private final JyAttachService jyAttachService;

    @GetMapping("/")
//    public String myBoardList(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "type", defaultValue = "A") String type, @RequestParam(value = "search", defaultValue = "") String search) {
//        Map searchMap = new HashMap();
//        searchMap.put("type", type);
//        searchMap.put("search", search);
//        int totalCnt = myBoardService.countAll(searchMap);//검색조건으로 찾은 총게시물의 수
//        int pageSize = 10;
//        int naviSize = 10;
//
//        //보고있는 페이지의 시작페이지와 끝페이지 정보를 가져온다
//        PageHandle ph = new PageHandle(totalCnt, page, pageSize, naviSize);//총게시물의 수,페이지사이즈, 네비사이즈
//
//        //검색조건+페이징처리
//        searchMap.put("offset", ((page - 1) * pageSize));//몇번부터 시작할건지
//        searchMap.put("pageSize", pageSize);//화면에 몇개씩 보여줄건지
//
//        ArrayList<MyBoard> myBoardList = myBoardService.selectList(searchMap);//게시글리스트 조회용
//
//        model.addAttribute("myBoardList", myBoardList);//보여질 정보
//        model.addAttribute("ph", ph);//화면에 보일 페이지네비
//        model.addAttribute("type", type);//검색조건
//        model.addAttribute("search", search);//검색내용
//        System.out.println("myBoardList" + myBoardList);
//        return "listBoard";
//    }

    public String myBoardList(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "type", defaultValue = "A") String type, @RequestParam(value = "search", defaultValue = "") String search) {

        Map searchMap = new HashMap();

        searchMap.put("type", type);
        searchMap.put("search", search);
        int totalCnt = myBoardService.countAll(searchMap);//검색조건으로 찾은 총게시물의 수

        int pageSize = 10;
        int naviSize = 10;

        //보고있는 페이지의 시작페이지와 끝페이지 정보를 가져온다
        PageHandle ph = new PageHandle(totalCnt, page, pageSize, naviSize);//총게시물의 수,페이지사이즈, 네비사이즈

        //검색조건+페이징처리
        searchMap.put("offset", ((page - 1) * pageSize));//몇번부터 시작할건지
        searchMap.put("pageSize", pageSize);//화면에 몇개씩 보여줄건지


        ArrayList<MyBoard> myBoardList = myBoardService.selectList(searchMap);//게시글리스트 조회용

        model.addAttribute("myBoardList", myBoardList);//보여질 정보
        model.addAttribute("ph", ph);//화면에 보일 페이지네비
        model.addAttribute("type", type);//검색조건
        model.addAttribute("search", search);//검색내용
        System.out.println("myBoardList" + myBoardList);
        return "listBoard";
    }
    /*
    해당번호의 게시글 리스트 조회
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


    private int deleteFiles(List<JyAttach> attachList) {
        //첨부파일이 없으면 그냥 메서드 끝내기
        if (attachList == null || attachList.size() == 0) {
            return 0;
        }
        //첨부파일이 있으면
        //collection.forEach(변수 -> 반복처리(변수)) //forEach
        attachList.forEach(jyAttach -> {
            Path file = Paths.get(jyAttach.getUploadPath() + "\\s_" + jyAttach.getUuid() + "_" + jyAttach.getOriginName());
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        return 1;
    }

    /*
    게시글 삭제 상태값 Y로 변경 -게시글은 상태값만 바꾸고 첨부파일은 삭제
    */
    @GetMapping("/deleteContent")
    public String myBoardContentDelete(long id) {

        int num = myBoardService.deleteContent(id);//게시물 삭제 결과가 있다면
        if (num > 0) {
            ArrayList<JyAttach> attachList = jyAttachService.attachList(id);
            int no = deleteFiles(attachList);//실제파일 삭제
            if (no > 0) {//실제파일이 삭제되었다면
                jyAttachService.deleteAttach(id);//db에서 삭제
                System.out.println("게시글 삭제처리후 첨부파일 삭제됨");

            }

        }

        return "redirect:/";
    }


    /*
    게시글 작성하기
    */

    @GetMapping("/writeForm")
    public String Write() {
        return "writeForm";//작성화면
    }

    /*
    첨부파일 포함 게시물 등록
    */
//년/월/일 폴더 생성 메서드
    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//mm은 분 MM은 월!

        Date date = new Date();

        String str = sdf.format(date);//오늘날짜를 지정한 포멧 형식으로 변환

        //format패턴의 "-"를 os의 구분자로 바꾸겠다 os마다 구분자가 달라서 File.separator 적어줘야함
        return str.replace("-", File.separator);


    }

    // 게시글 등록
    @PostMapping("/insertContent")
    public String myBoardInsertContent(MultipartFile[] uploadFile, MyBoard board, Model model) {
        myBoardService.insertContent(board);//db에 입력
        Long id = myBoardService.selectId();

        String originUploadFileName = "";
        String changeUploadFileName = "";

        for (MultipartFile multipartFile : uploadFile) {

            originUploadFileName = multipartFile.getOriginalFilename();//파일원본명
        }

        if (originUploadFileName.length() > 0) {

            List fileList = new ArrayList();
            String uploadFolder = "C:\\upload"; //파일이 저장될 상위경로

            //같은폴더에 파일이 많으면 속도 저하 개수제한 문제등이 생긴다 날짜로 폴더 만들어주기
            File uploadPath = new File(uploadFolder, getFolder());//File(상위경로,하위경로?)

            if (uploadPath.exists() == false) {
                uploadPath.mkdirs();//mkdirs(); 폴더 만드는 메서드
                System.out.println("폴더생성");
            } else {
                System.out.println("이미 폴더가 있습니다");
            }


            for (MultipartFile multipartFile : uploadFile) {

                originUploadFileName = multipartFile.getOriginalFilename();//파일원본명
                long size = multipartFile.getSize();//파일사이즈

                //동일한 파일명일때 기존파일 덮어버리는 문제 해결위해 UUID
                UUID uuid = UUID.randomUUID();
                changeUploadFileName = uuid.toString() + "-" + originUploadFileName;//랜덤uuid+"-"+원본명
                File saveFile = new File(uploadPath, changeUploadFileName);

                JyAttach attach = new JyAttach();
                attach.setUuid(changeUploadFileName);
                attach.setUploadPath(String.valueOf(uploadPath));
                attach.setOriginName(originUploadFileName);
                attach.setBno(id);

                System.out.println("attatch에 담긴 값 " + attach.toString());
                fileList.add(attach);

                try {
                    multipartFile.transferTo(saveFile);//파일에 저장 try Catch해주기
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }//end for


            System.out.println("입력한값" + board);


            jyAttachService.insertFile((ArrayList) fileList);
        }
        return "redirect:/";

    }

    /*
    첨부파일 다운로드 정리하기
    */
    @GetMapping(value = "/downloadFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(String uploadPath, String fileName) {

        System.out.println("파일의경로는" + uploadPath + "파일의 이름은" + fileName);
        //파일경로와 폴더 사이에 역슬래시 있어야함!!!
        Resource resource = new FileSystemResource(uploadPath + "\\" + fileName);//역슬래시를 한번 문자열으로 출력하려면 \\두번!

        String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Disposition", "attachment; " +
                    "fileName=" + new String(resourceName.getBytes("UTF-8"),
                    "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/updateContent")
    public String myBoardUpdateContent(MyBoard board, MultipartFile[] uploadFile, @RequestParam(value = "attBno", required = false) List<Long> attList) {// )HttpServletRequest request

        System.out.println("업데이트 컨텐츠 attList" + attList);
        List attFileList = new ArrayList();
        System.out.println("이프문들어가기전");

        if (attList != null) {
            for (Long attBno : attList) {
                attFileList.add(attBno);
            }

            System.out.println("attFileList" + attFileList);
            jyAttachService.deleteOnlyAttach(attFileList);
        }
        System.out.println("board" + board);
        myBoardService.updateContent(board);//게시글수정

        String originUploadFileName = "";
        String changeUploadFileName = "";

        for (MultipartFile multipartFile : uploadFile) {

            originUploadFileName = multipartFile.getOriginalFilename();//파일원본명
            System.out.println("multipartFile" + multipartFile);
        }

        List fileList = new ArrayList();
        if (originUploadFileName.length() > 0) {

            String uploadFolder = "C:\\upload"; //파일이 저장될 상위경로

            //같은폴더에 파일이 많으면 속도 저하 개수제한 문제등이 생긴다 날짜로 폴더 만들어주기
            File uploadPath = new File(uploadFolder, getFolder());//File(상위경로,하위경로?)

            if (uploadPath.exists() == false) {
                uploadPath.mkdirs();//mkdirs(); 폴더 만드는 메서드
                System.out.println("폴더생성");
            } else {
                System.out.println("이미 폴더가 있습니다");
            }

            for (MultipartFile multipartFile : uploadFile) {

                originUploadFileName = multipartFile.getOriginalFilename();//파일원본명
                long size = multipartFile.getSize();//파일사이즈

                System.out.println("uploadFileName " + originUploadFileName + "size" + size);

                //동일한 파일명일때 기존파일 덮어버리는 문제 해결위해 UUID
                UUID UUid = UUID.randomUUID();
                changeUploadFileName = UUid.toString() + "-" + originUploadFileName;//랜덤uuid+"-"+원본명
                File saveFile = new File(uploadPath, changeUploadFileName);

                JyAttach attach = new JyAttach();
                attach.setUuid(changeUploadFileName);
                attach.setUploadPath(String.valueOf(uploadPath));
                attach.setOriginName(originUploadFileName);
                attach.setBno(board.getId());

                System.out.println("attach" + attach);

                fileList.add(attach);

                try {
                    multipartFile.transferTo(saveFile);//파일에 저장 try Catch해주기
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


            System.out.println("입력한값" + board);

            jyAttachService.insertFile((ArrayList) fileList);
        }

        return "redirect:/";

    }


}
