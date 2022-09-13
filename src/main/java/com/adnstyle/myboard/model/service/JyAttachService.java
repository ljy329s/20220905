package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.domain.JyAttach;
import com.adnstyle.myboard.model.repository.JyAttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JyAttachService {
    private final JyAttachRepository jyAttachRepository;

    public void insertFile(ArrayList fileList) {

        jyAttachRepository.insertFile(fileList);
    }

    public ArrayList<JyAttach> attachList(long id) {
        return jyAttachRepository.attachList(id);
    }
}
