package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.JyAttach;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

//Attach 관련
@Mapper//Repository어노테이션 다니까 에러남
public interface JyAttachRepository {
    void insertFile(Map fileMap);//첨부파일등록

    void deleteFile(String uuid);//첨부파일 삭제

    List<JyAttach> findByBno(long bno);//게시물의 번호로 첨부파일 조회
}
