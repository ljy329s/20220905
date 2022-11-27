package com.adnstyle.myboard.model.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
//@Component 쓰지않고 Application에 @configurationPropertisScan달아주면 된다.
@ConfigurationProperties(prefix = "spring.file-upload")
public class FileUploadYml {

    /**
     * 파일이 저장될 경로
     */
    private String saveDir;
}

