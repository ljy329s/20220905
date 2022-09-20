package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.MyBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface MyBoardRepository {

    /**
     * 관리자가 볼 게시판 리스트
     */
    ArrayList<MyBoard> selectList(Map searchMap);

    /**
     * 고객들이 볼 게시판 리스트
     */
    ArrayList<MyBoard> myBoardPage(Map pageMap);

    /**
     * 게시글 총 갯수
     */
    int countAll(Map searchMap);

    /**
     * 게시글 상세조회
     */
    ArrayList<MyBoard> selectContent(Long id);

    /**
     * 게시글 클릭시 조회수 증가
     */
    void updateCount(Long id);

    /**
     * 게시글 삭제 처리(상태값변경)
     */
    int deleteContent(Long id);

    /**
     * 게시글 등록
     */
    void insertContent(MyBoard board);

    /**
     * 게시글 수정
     */
    void updateContent(MyBoard board);


    /**
     * 최신 게시글 번호 가져오기
     */
    Long selectId();
}
  
