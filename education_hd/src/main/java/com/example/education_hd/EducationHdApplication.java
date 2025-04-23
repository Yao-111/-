package com.example.education_hd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.education_hd.repository")
public class EducationHdApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducationHdApplication.class, args);
    }

}
