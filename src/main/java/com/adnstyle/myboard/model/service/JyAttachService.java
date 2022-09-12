package com.adnstyle.myboard.model.service;

import com.adnstyle.myboard.model.repository.JyAttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class JyAttachService {
    private final JyAttachRepository jyAttachRepository;
    public void insertFile(Map<String, Object> fileMap) {
        jyAttachRepository.insertFile(fileMap);
    }
}
