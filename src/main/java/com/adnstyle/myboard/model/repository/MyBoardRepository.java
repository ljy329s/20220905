package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.domain.PageHandle;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface MyBoardRepository {

    ArrayList<MyBoard> selectList(Map searchMap);

    ArrayList<MyBoard> myBoardPage(Map pageMap);

    int countAll(Map searchMap);
   ArrayList<MyBoard> selectContent(Long id);

    void updateCount(Long id);

    int deleteContent(Long id);

    void insertContent(MyBoard board);

    //게시글 수정하기
    void updateContent(MyBoard board);

    //검색
   ArrayList SearchCondition(Map searchMap);

    Long selectId();
}
  
