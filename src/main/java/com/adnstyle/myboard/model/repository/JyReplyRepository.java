package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.JyReply;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface JyReplyRepository {
    void insertReply(JyReply jyReply);
}
