package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.JyAttach;
import org.springframework.stereotype.Repository;

import java.util.List;

//Attach 관련
@Repository
public interface JyAttachRepository {
    public void insertFile(JyAttach attach);//첨부파일등록

    public void deleteFile(String uuid);//첨부파일 삭제

    public List<JyAttach> findByBno(long bno);//게시물의 번호로 첨부파일 조회
}
