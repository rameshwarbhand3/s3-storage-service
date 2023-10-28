package com.ram.storage.controller;

import com.ram.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StorageServiceController {
     private static final Logger LOGGER  = LoggerFactory.getLogger(StorageServiceController.class);
    @Autowired
    private StorageService storageService;
    @PostMapping("/storage/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile multipartFile,@RequestParam("fileName") String fileName){
        String eTag = storageService.upload(multipartFile, fileName);
        LOGGER.info("file has been uploaded successfully, eTag={}",eTag);
        return ResponseEntity.ok().build();
    }
}
