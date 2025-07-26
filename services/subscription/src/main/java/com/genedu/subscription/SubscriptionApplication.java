package com.genedu.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan(basePackages = {"com.genedu.subscription", "com.genedu.commonlibrary"})
public class SubscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionApplication.class, args);
    }

}
