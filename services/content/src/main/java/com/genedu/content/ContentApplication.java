package com.genedu.content;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication()
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.genedu.content", "com.genedu.commonlibrary"})
public class ContentApplication {
	public static void main(String[] args) {
		SpringApplication.run(ContentApplication.class, args);
		log.info("Content Service is running...");
	}
}
