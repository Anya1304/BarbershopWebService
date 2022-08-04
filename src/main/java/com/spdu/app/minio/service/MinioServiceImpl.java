package com.spdu.app.minio.service;

import io.minio.*;
import io.minio.messages.Bucket;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private static final int PART_SIZE = 15000000;

    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void uploadFile(MultipartFile file, String bucketName) {
        if (!isBucketExists(bucketName)) {
            createBucket(bucketName);
        }
        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            putFile(file, bucketName, inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileName, String bucketName) {
        if (!isBucketExists(bucketName)) {
            throw new IllegalArgumentException("bucket with name" + bucketName + "don`t exist");
        }
        try {
            minioClient.removeObject(RemoveObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public byte[] getFile(String fileName, String bucketName) {
        if (!isBucketExists(bucketName)) {
            throw new IllegalArgumentException("bucket with name" + bucketName + "don`t exist");
        }
        try {
            GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            return object.readAllBytes();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<String> getBucketsName() {
        List<String> list;
        try {
            list = minioClient.listBuckets().stream().map(Bucket::name).toList();
            return list;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void createBucket(String bucketName) {
        try {
            if (!isBucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private void putFile(MultipartFile file, String bucketName, InputStream inputStream) throws Exception {
        String originalFilename = file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(originalFilename.substring(0,
                                originalFilename.lastIndexOf(".")) +
                                UUID.randomUUID().toString().replace("-", "")
                                + originalFilename.substring(originalFilename.lastIndexOf(".")))
                        .stream(inputStream, -1, PART_SIZE)
                        .contentType(file.getContentType())
                        .build());
    }

    private boolean isBucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs
                    .builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
