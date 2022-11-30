package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.JyAttach;
import com.adnstyle.myboard.model.repository.JyAttachRepository;
import com.adnstyle.myboard.model.repository.JyReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JyAttachService {
    private final JyAttachRepository jyAttachRepository;

    private final JyReplyRepository jyReplyRepository;


    //년/월/일 폴더 생성 메서드
    public String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//mm은 분 MM은 월!

        Date date = new Date();

        String str = sdf.format(date);//오늘날짜를 지정한 포멧 형식으로 변환

        //format패턴의 "-"를 os의 구분자로 바꾸겠다 os마다 구분자가 달라서 File.separator 적어줘야함
        return str.replace("-", File.separator);
    }

    @Transactional
    public void insertFile(ArrayList fileList) {
        jyAttachRepository.insertFile(fileList);
    }

    public List<JyAttach> attachList(long id) {
        return jyAttachRepository.attachList(id);
    }

    @Transactional
    public int deleteFiles(List<JyAttach> attachList) {
        //첨부파일이 없으면 그냥 메서드 끝내기
        if (attachList == null || attachList.size() == 0) {
            return 0;
        }
        //첨부파일이 있으면 collection.forEach(변수 -> 반복처리(변수)) //forEach
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

    @Transactional
    public void deleteAttach(Long id) {
        jyAttachRepository.deleteAll(id);
    }

    /**
     * 첨부파일 완전 삭제
     */
    @Transactional
    public void deleteOnlyAttach(List<Long> attlist) {
        jyAttachRepository.deleteOnlyAttach(attlist);
    }

    /**
     * 첨부파일 상태값 삭제로 변경
     */
    @Transactional
    public void delAttachYn(List<Long> attlist) {
        jyAttachRepository.delAttachYn(attlist);
    }

}
