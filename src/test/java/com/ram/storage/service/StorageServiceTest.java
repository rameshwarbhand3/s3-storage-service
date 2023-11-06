package com.ram.storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.ram.storage.configuration.S3Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StorageServiceTest {
    private StorageService storageService;
    @Mock
    private AmazonS3 s3Client;

    @Mock
    private S3Configuration s3Configuration;


    @BeforeEach
    void setUp() {
        //amazonS3 = mock(AmazonS3.class);
        //s3Configuration = mock(S3Configuration.class);
        storageService = new StorageService(s3Configuration, s3Client);
    }


    @Test
    public void testUploadSuccess() {
        // given
        MultipartFile multiPartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", "Hello, My name is Ram".getBytes());

        when(s3Configuration.getBucketName()).thenReturn("test-bucket");

        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.setETag("eTag123456789");
        when(s3Client.putObject(eq("test-bucket"), eq("test.txt"), any(InputStream.class), any(ObjectMetadata.class)))
                .thenReturn(putObjectResult);

        //when
        String actual = storageService.upload(multiPartFile, "test.txt");

        //then
        assertEquals("eTag123456789", actual);
    }

    @Test
    public void testUploadNotSuccess(){
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.txt","test.txt","text/plain","Hello, This is a Ram".getBytes());
         when(s3Configuration.getBucketName()).thenReturn("test-bucket");

         PutObjectResult putObjectResult = new PutObjectResult();
         putObjectResult.setETag("eTag");

         when(s3Client.putObject(eq("test-bucket"),eq("test.txt"),any(InputStream.class),any(ObjectMetadata.class)))
        .thenReturn(putObjectResult);

         //when
        String actual = storageService.upload(multipartFile,"test.txt");

        //then
        assertNotEquals("e12344",actual);
    }

    @Test
    public void testDownloadSuccess() {
        //given
        when(s3Configuration.getBucketName()).thenReturn("test-bucket");
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new ByteArrayInputStream("This is my bucket content".getBytes()));
        when(s3Client.getObject(eq("test-bucket"), anyString())).thenReturn(s3Object);

        //when
        byte[] data = storageService.download("test.txt");

        //then
        assertEquals("This is my bucket content", new String(data));
    }

    @Test
    public void testDownLoadNotSuccess(){
        //given
        when(s3Configuration.getBucketName()).thenReturn("test-bucket");
        S3Object s3Object  = new S3Object();
        s3Object.setObjectContent(new ByteArrayInputStream("This is my bucket Content".getBytes()));
        when(s3Client.getObject(eq("test-bucket"),anyString())).thenReturn(s3Object);

        //when
        byte[] data = storageService.download("test.txt");

        //when
        assertNotEquals("Hello, This is a S3 storage service",new String(data));
    }

}