package com.genedu.media.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public abstract class AWSClientConfig {
    @Value("${aws.credentials.access-key}")
    protected String awsAccessKey;

    @Value("${aws.credentials.secret-key}")
    protected String awsSecretKey;

    @Value("${aws.region}")
    protected String awsRegion;

    protected AwsCredentialsProvider amazonAWSCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey));
    }
}
