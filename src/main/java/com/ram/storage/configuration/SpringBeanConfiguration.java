package com.ram.storage.configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeanConfiguration {
    @Autowired
    private S3Configuration s3Configuration;
    @Bean
    public AmazonS3 s3Client(){
        //ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider("ram");
        return AmazonS3ClientBuilder.standard()
                //.withCredentials(profileCredentialsProvider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Configuration.getEndpoint(), s3Configuration.getRegion()))
                //.withRegion(Regions.fromName(s3Configuration.getRegion()))

                .build();
    }
}
