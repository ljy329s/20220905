package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.MyBoard;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Mapper
public interface MyBoardRepository {
    ArrayList<MyBoard> selectList();

    ArrayList<MyBoard> selectContent(Long id);

    void updateCount(Long id);

    void deleteContent(Long id);

    void insertContent(MyBoard board);

    void updateContent(MyBoard board);
}
