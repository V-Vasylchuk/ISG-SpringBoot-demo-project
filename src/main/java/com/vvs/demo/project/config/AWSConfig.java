package com.vvs.demo.project.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {
    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion((region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
