package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.JyAttach;
import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.repository.MyBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyBoardService {

//    @Value("${servlet.multipart.location}")
//    private String fileRealPath;

    private final MyBoardRepository myBoardRepository;

    private final JyAttachService jyAttachService;


    public ArrayList<MyBoard> selectList(Map searchMap) {

        return myBoardRepository.selectList(searchMap);

    }

    public int countAll(Map searchMap) {
        return myBoardRepository.countAll(searchMap);
    }

    public ArrayList<MyBoard> myBoardPage(Map pageMap) {
        return myBoardRepository.myBoardPage(pageMap);
    }

    public ArrayList<MyBoard> selectContent(Long id) {

        myBoardRepository.updateCount(id);
        return myBoardRepository.selectContent(id);
    }

    @Transactional
    public void deleteContent(long id) {
        int num = myBoardRepository.deleteContent(id);

        if (num > 0) {
            ArrayList<JyAttach> attachList = jyAttachService.attachList(id);
            int no = jyAttachService.deleteFiles(attachList);//실제파일 삭제
            if (no > 0) {
                jyAttachService.deleteAttach(id);
                System.out.println("게시글 삭제처리후 첨부파일 삭제됨");
            }
        }
    }

    @Transactional
    public void insertContent(MyBoard board, MultipartFile[] uploadFile) {

        long id = 0;//등록한 글번호

        if(board.getGroupBno()==null){//일반게시글등록시
            myBoardRepository.insertContent(board);
            id = myBoardRepository.selectId();//등록한 게시글 번호 가져오기
            myBoardRepository.updateGroupBno(id);//글 등록후 불러와서 그룹번호 업데이트 해줄예정
        }else{//답글등록시(그룹번호있으니)
            String anstitle = "[답글]"+board.getTitle();
            board.setTitle(anstitle);
            System.out.println("board"+board);
            myBoardRepository.insertAnswer(board);
        }
        String originUploadFileName = "";
        String changeUploadFileName = "";

        for (MultipartFile multipartFile : uploadFile) {
            originUploadFileName = multipartFile.getOriginalFilename();//파일원본명
        }
        if (originUploadFileName.length() > 0) {
            List fileList = new ArrayList();
           String uploadFolder = "C:\\upload"; //파일이 저장될 상위경로


            //같은폴더에 파일이 많으면 속도 저하 개수제한 문제등이 생긴다 날짜로 폴더 만들어주기
            File uploadPath = new File(uploadFolder, jyAttachService.getFolder());//File(상위경로,하위경로?)

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

            jyAttachService.insertFile((ArrayList) fileList);//첨부파일등록

        }
    }

    //게시글 수정하기

    @Transactional
    public void updateContent(MyBoard board, MultipartFile[] uploadFile) {

        myBoardRepository.updateContent(board);

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
            File uploadPath = new File(uploadFolder, jyAttachService.getFolder());//File(상위경로,하위경로?)

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
    }

}