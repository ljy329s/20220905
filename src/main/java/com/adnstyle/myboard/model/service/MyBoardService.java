package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.domain.PageHandle;
import com.adnstyle.myboard.model.repository.MyBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class MyBoardService {
  @Autowired
  MyBoardRepository myBoardRepository;

    public ArrayList<MyBoard> selectList(Map pageMap) {
    return myBoardRepository.selectList(pageMap);

    }
    public ArrayList<MyBoard> myBoardPage(Map pageMap) {
        return myBoardRepository.myBoardPage(pageMap);
    }

    public int countAll(){
        return myBoardRepository.countAll();
    }

    public ArrayList<MyBoard> selectContent(Long id) {
        myBoardRepository.updateCount(id);
        System.out.println("id는"+id);
        return myBoardRepository.selectContent(id);
    }

    public void deleteContent(MyBoard board) {
        myBoardRepository.deleteContent(board);
    }

    public void insertContent(MyBoard board) {
        myBoardRepository.insertContent(board);
    }

    //게시글 수정하기

    public void updateContent(MyBoard board) {
        myBoardRepository.updateContent(board);

    }


    public ArrayList SearchCondition(Map searchMap) {
        return myBoardRepository.SearchCondition(searchMap);//다른쿼리들을 인클루드하는 대장쿼리?
    }
}
