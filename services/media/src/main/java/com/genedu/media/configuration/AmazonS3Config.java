package com.genedu.media.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class AmazonS3Config extends AWSClientConfig {
    @Value("${aws.s3.endpoint}")
    private String awsS3EndPoint;

    @Bean
    public S3Client s3() {
        return S3Client.builder()
                .endpointOverride(URI.create(awsS3EndPoint))
                .region(Region.of(awsRegion))
                .credentialsProvider(amazonAWSCredentialsProvider())
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();
    }
}
