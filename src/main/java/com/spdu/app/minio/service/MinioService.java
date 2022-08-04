package com.spdu.app.minio.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioService {
    void uploadFile(MultipartFile file, String bucketName);

    void deleteFile(String fileName, String bucketName);

    byte[] getFile(String fileName, String bucketName);

    List<String> getBucketsName();
}
