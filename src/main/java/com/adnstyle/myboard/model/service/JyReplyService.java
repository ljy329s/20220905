package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.JyReply;
import com.adnstyle.myboard.model.repository.JyReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JyReplyService {

    private final JyReplyRepository jyReplyRepository;

    /**
     * 댓글등록
     */
    @Transactional
    public void insertReply(JyReply jyReply) {
        jyReplyRepository.insertReply(jyReply);//리플등록
        Long bno = jyReplyRepository.selectMaxReBno();//게시글번호 조회해오기(그룹번호로 넣을것)
        jyReplyRepository.updateGroupBno(bno);//그룹번호 업데이트 해주기

    }

    public List selectReplyList(long id) {
        return jyReplyRepository.selectReplyList(id);//게시글 번호로 조회해서 화면에 보여주기
    }

    @Transactional
    public void insertChildReply(JyReply jyReply) {
        jyReplyRepository.updateOrderBno(jyReply);//1.order업데이트 해주기
        jyReplyRepository.insertChildReply(jyReply);//2.하위댓글 insert해주기
    }

    public void deleteReply(Long delReBno) {
        jyReplyRepository.deleteReply(delReBno);
    }
}
