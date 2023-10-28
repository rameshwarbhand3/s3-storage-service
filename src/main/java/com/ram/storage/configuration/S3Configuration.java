package com.ram.storage.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
@Configuration
public class S3Configuration {
    private String bucketName;
    private String region;

}
