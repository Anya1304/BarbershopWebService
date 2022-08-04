package com.spdu.app.service;

import com.spdu.app.minio.service.MinioServiceImpl;
import io.minio.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MinioServiceTest {
    @Mock
    private MinioClient minioClient;
    @InjectMocks
    private MinioServiceImpl minioService;

    @Test
    public void whenDeleteFileInDoesntExistBucketThenShouldThrowException() {
        //GIVEN
        String bucketName = "bucket";
        //THEN
        assertThatThrownBy(() -> minioService.deleteFile("file", bucketName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("bucket with name" + bucketName + "don`t exist");
    }

    @Test
    public void whenDeleteFileThenShouldCallClientMethod() throws Exception {
        //GIVEN
        String bucketName = "bucket";
        given(minioClient.bucketExists(any())).willReturn(true);
        String fileName = "file";
        //WHEN
        minioService.deleteFile(fileName, bucketName);
        //THEN
        ArgumentCaptor<RemoveObjectArgs> captor = ArgumentCaptor.forClass(RemoveObjectArgs.class);
        verify(minioClient).removeObject(captor.capture());
        assertThat(captor.getValue().bucket()).isEqualTo(bucketName);
    }

    @Test
    public void whenUploadFileInDoesntExistsBucketThenShouldCreateThisBucket() throws Exception {
        //GIVEN
        String bucketName = "bucket";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());
        //WHEN
        minioService.uploadFile(mockMultipartFile, bucketName);
        //THEN
        verify(minioClient).makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    @Test
    public void whenUploadFileInExistsBucketThenShouldCallClientMethod() throws Exception {
        //GIVEN
        String bucketName = "bucket";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());
        given(minioClient.bucketExists(any())).willReturn(true);
        //WHEN
        minioService.uploadFile(mockMultipartFile, bucketName);
        //THEN
        ArgumentCaptor<PutObjectArgs> captor = ArgumentCaptor.forClass(PutObjectArgs.class);
        verify(minioClient).putObject(captor.capture());
        assertThat(captor.getValue().bucket()).isEqualTo(bucketName);
        assertThat(captor.getValue().contentType()).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
        assertThat(captor.getValue().object()).contains(mockMultipartFile.getOriginalFilename()
                .substring(0, mockMultipartFile.getOriginalFilename().indexOf(".")));
    }

    @Test
    public void whenGetFileInDoesntExistsBucketThenShouldThrowException() {
        //GIVEN
        String bucketName = "bucket";
        //THEN
        assertThatThrownBy(() -> minioService.getFile("file", bucketName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("bucket with name" + bucketName + "don`t exist");
    }

    @Test
    public void whenGetFileInExistsBucketThenShouldCallClientMethod() throws Exception {
        //GIVEN
        String bucketName = "bucket";
        String fileName = "file";

        given(minioClient.bucketExists(any())).willReturn(true);
        GetObjectResponse objectResponse = mock(GetObjectResponse.class);
        given(minioClient.getObject(any())).willReturn(objectResponse);
        //WHEN
        minioService.getFile(fileName, bucketName);
        //THEN
        verify(objectResponse).readAllBytes();
    }
}
