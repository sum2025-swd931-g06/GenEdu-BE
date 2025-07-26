package com.genedu.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.genedu.project", "com.genedu.commonlibrary"})
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(
                ProjectApplication.class, args);
        log.info("Project Service is running...");
    }
}
