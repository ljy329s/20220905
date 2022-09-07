package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.MyBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface MyBoardRepository {

    ArrayList<MyBoard> selectList(Map pageMap);

    ArrayList<MyBoard> myBoardPage(Map pageMap);

    int countAll();
    ArrayList<MyBoard> selectContent(Long id);

    void updateCount(Long id);

    void deleteContent(MyBoard board);

    void insertContent(MyBoard board);

    //게시글 수정하기
    void updateContent(MyBoard board);


}
  
