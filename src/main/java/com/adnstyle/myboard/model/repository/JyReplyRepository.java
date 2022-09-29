package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.JyReply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JyReplyRepository {
    void insertReply(JyReply jyReply);

    Long selectMaxReBno();

    Long updateGroupBno(Long bno);

    List<JyReply> selectReplyList(Long id);


    void updateOrderBno(JyReply reply);

    void insertChildReply(JyReply jyReply);

    void deleteReply(Long delReBno);

    JyReply selectReply(Long reBno);
}
