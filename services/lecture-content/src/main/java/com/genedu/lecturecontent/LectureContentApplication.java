package com.genedu.lecturecontent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.genedu.lecturecontent", "com.genedu.commonlibrary"})
public class LectureContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(LectureContentApplication.class, args);
        log.info("Lecture Content Service is running...");
    }

}
