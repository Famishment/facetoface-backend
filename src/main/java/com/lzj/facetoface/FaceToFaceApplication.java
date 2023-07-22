package com.lzj.facetoface;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.lzj.facetoface.mapper")
@EnableScheduling // 在SpringBoot中开启对定时任务的支持（相当于任务调度机制 ）
public class FaceToFaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceToFaceApplication.class, args);
    }

}
