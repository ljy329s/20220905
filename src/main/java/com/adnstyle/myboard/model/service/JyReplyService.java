package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.JyReply;
import com.adnstyle.myboard.model.repository.JyReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JyReplyService {

    private final JyReplyRepository jyReplyRepository;
    public void insertReply(JyReply jyReply) {
        jyReplyRepository.insertReply(jyReply);
    }
}
