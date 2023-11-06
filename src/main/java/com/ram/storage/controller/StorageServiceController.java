package com.ram.storage.controller;

import com.ram.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/storage")
public class StorageServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceController.class);
    @Autowired
    private StorageService storageService;

    //@PreAuthorize("hasAuthority('SCOPE_openid')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                        @RequestParam(value = "fileName", required = false) String fileName) {
        String eTag = storageService.upload(multipartFile, fileName);
        LOGGER.info("file has been uploaded successfully, eTag={}", eTag);
        return ResponseEntity.ok().build();
    }

    //@PreAuthorize("hasAuthority('SCOPE_openid')")
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam(value = "objectKey") String objectKey) throws IOException {
        byte[] data = storageService.download(objectKey);
        ByteArrayResource byteArrayResource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=" + objectKey )
                .header("Cache-Control", "no-cache")
                .body(byteArrayResource);
    }
}
