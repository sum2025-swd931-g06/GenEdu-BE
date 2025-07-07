package com.genedu.lecturecontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.genedu.lecturecontent", "com.genedu.commonlibrary"})
public class LectureContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(LectureContentApplication.class, args);
    }

}
