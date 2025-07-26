package com.genedu.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.genedu.media", "com.genedu.commonlibrary"})
public class MediaApplication {
    private static final Logger log = LoggerFactory.getLogger(MediaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
        log.info("Media Service is running...");
    }
}
