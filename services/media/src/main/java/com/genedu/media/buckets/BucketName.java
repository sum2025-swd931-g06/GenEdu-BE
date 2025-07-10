package com.genedu.media.buckets;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@Configuration
@PropertySource(value = "classpath:buckets.properties")
public class BucketName {
    @Value("${genedu-bucket}")
    private String geneduBucket;
}

