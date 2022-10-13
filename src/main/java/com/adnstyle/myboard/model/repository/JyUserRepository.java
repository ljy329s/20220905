package com.adnstyle.myboard.model.repository;

import com.adnstyle.myboard.model.domain.JyUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JyUserRepository {
    void insertNewUser(JyUser jyUser);

    int checkId(String userId);

    int checkEmail(String userEmail);

    JyUser selectUser(String userId);
}
