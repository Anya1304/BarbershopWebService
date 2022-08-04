package com.spdu.app.minio.controller;

import com.spdu.app.minio.service.MinioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/minio/buckets")
@SecurityRequirement(name = "salon")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping(value = "/{bucketName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("permitAll")
    public void uploadFile(@PathVariable String bucketName, @RequestBody(required = false) MultipartFile file) {
        minioService.uploadFile(file, bucketName);
    }

    @DeleteMapping("/{bucketName}/{fileName}")
    public void deleteFile(@PathVariable String bucketName, @PathVariable String fileName) {
        minioService.deleteFile(fileName, bucketName);
    }

    @GetMapping("/{bucketName}/{fileName}")
    public @ResponseBody
    byte[] getFile(@PathVariable String bucketName, @PathVariable String fileName) throws IOException {
        return minioService.getFile(fileName, bucketName);
    }
}
