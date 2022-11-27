package com.adnstyle.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.adnstyle.myboard")
@ConfigurationPropertiesScan//이 어노테이션을 붙이면 yml값을 가져오는 클래스에 @component를 붙이지 않아도 알아서 바인딩 된다.
public class MyBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBoardApplication.class, args);

    }

}
