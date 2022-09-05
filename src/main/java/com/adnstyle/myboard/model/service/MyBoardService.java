package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.domain.PageHandle;
import com.adnstyle.myboard.model.repository.MyBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyBoardService {
  @Autowired
  MyBoardRepository myBoardRepository;

    public ArrayList<MyBoard> selectList(int page) {
    return myBoardRepository.selectList(page);

    }
    /*public ArrayList<MyBoard> pageList(int page,int start,int end) {
        return myBoardRepository.pageList(page,start,end);

    }*/
    public int countAll(){
        return myBoardRepository.countAll();
    }
    public ArrayList<MyBoard> selectContent(Long id) {
        myBoardRepository.updateCount(id);
        System.out.println("id는"+id);
        return myBoardRepository.selectContent(id);
    }

    public void deleteContent(Long id) {
        myBoardRepository.deleteContent(id);
    }

    public void insertContent(MyBoard board) {
        myBoardRepository.insertContent(board);
    }

    //게시글 수정하기
    /*
    public void updateContent(MyBoard board) {
        myBoardRepository.updateContent(board);

    }*/


}
