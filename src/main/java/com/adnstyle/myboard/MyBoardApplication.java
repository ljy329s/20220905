package com.adnstyle.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.adnstyle.myboard")
public class MyBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBoardApplication.class, args);

    }

}
