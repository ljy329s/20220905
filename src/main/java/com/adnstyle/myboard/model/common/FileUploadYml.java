package com.adnstyle.myboard.model.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.file-upload")
public class FileUploadYml {

    /**
     * 파일이 저장될 경로
     */
    private String saveDir;
}
