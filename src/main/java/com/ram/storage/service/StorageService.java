package com.ram.storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ram.storage.configuration.S3Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class StorageService {

    private S3Configuration s3Configuration;

    private AmazonS3 s3Client;

    @Autowired
    public StorageService(S3Configuration s3Configuration, AmazonS3 s3Client) {
        this.s3Configuration = s3Configuration;
        this.s3Client = s3Client;
    }

    public String upload(MultipartFile file, String fileName) {
        ObjectMetadata objectMetadata = getObjectMetadata(file);
        if (fileName == null || fileName.isBlank()) {
            fileName = file.getName();
        }
        try {
            PutObjectResult putObjectResult = s3Client.putObject(s3Configuration.getBucketName(), fileName, file.getInputStream(), objectMetadata);
            return putObjectResult.getETag();
        } catch (IOException e) {
            throw new RuntimeException("Error in file upload: " + e.getMessage(), e);
        }

    }


    public void download(String objectKey) throws IOException {
        S3Object s3Object = s3Client.getObject(s3Configuration.getBucketName(), objectKey);
        S3ObjectInputStream s3is = s3Object.getObjectContent();
        FileOutputStream fos = new FileOutputStream(s3Object.getKey());

        byte[] read_buf = new byte[1024];
        int read_len = 0;
        while ((read_len = s3is.read(read_buf)) > 0) {
            fos.write(read_buf, 0, read_len);
        }
        s3is.close();
        fos.close();
    }

    private static ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

}



