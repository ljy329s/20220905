package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.JyAttach;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface JyAttachRepository {

    /**
     * 첨부파일 등록
     */
    void insertFile(ArrayList fileList);//첨부파일등록

    /**
     * 게시글 번호로 첨부파일 리스트 불러오기
     */
    List<JyAttach> attachList(Long id);

    /**
     * 게시글에 포함된 모든 첨부파일 삭제
     */
    void deleteAll(Long id);

    /**
     * 게시글 수정시 특정 첨부파일만 삭제
     */
    void deleteOnlyAttach(List<Long> attlist);

}
