package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.JyAttach;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

//Attach 관련
@Mapper//Repository어노테이션 다니까 에러남
public interface JyAttachRepository {
    void insertFile(ArrayList fileList);//첨부파일등록

    ArrayList<JyAttach> attachList(long id);

    //db에서 첨부파일 삭제
    void deleteAll(Long id);

    void deleteOnlyAttach(List<Long> attlist);


//    void deleteFile(String uuid);//첨부파일 삭제

}
