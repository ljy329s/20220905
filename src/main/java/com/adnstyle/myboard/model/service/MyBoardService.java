package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.MyBoard;
import com.adnstyle.myboard.model.repository.MyBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyBoardService {
    @Autowired
    public MyBoardRepository myBoardRepository;

    public ArrayList<MyBoard> selectList() {
    return myBoardRepository.selectList();

    }

    public ArrayList<MyBoard> selectContent(Long id) {
        myBoardRepository.updateCount(id);
        return myBoardRepository.selectContent(id);
    }

    public void deleteContent(Long id) {
        myBoardRepository.deleteContent(id);
    }

    public void insertContent(MyBoard board) {
        myBoardRepository.insertContent(board);
    }

    public void updateContent(MyBoard board) {
        myBoardRepository.updateContent(board);
    }
}
