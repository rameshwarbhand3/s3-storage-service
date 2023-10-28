package com.ram.storage.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Disabled
class StorageServiceTest {

    private StorageService storageService;


    @Test
    public void testUpload(){
        MultipartFile mulitPartFile = new MockMultipartFile("test.txt","test.txt","text/plain","Hello, My name is Ram".getBytes());
        String eTag = storageService.upload(mulitPartFile, "");
        Assertions.assertNotNull(eTag);
    }
    @Test
    public void testDownload() throws IOException {
        storageService.download("myfile.md");
    }

}