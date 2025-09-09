package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.config.property.AwsS3Property;
import com.ecommerce.productservice.dto.DownloadUrlDto;
import com.ecommerce.productservice.dto.UploadUrlDto;
import com.ecommerce.productservice.service.S3StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3StorageServiceImpl implements S3StorageService {

    AwsS3Property awsS3Property;
    S3Client s3Client;
    S3Presigner s3Presigner;

    static String CONTENT_TYPE = "image/jpeg";
    static Duration SIGNATURE_DURATION = Duration.ofMinutes(10);

    @Override
    public UploadUrlDto generateUploadUrl(String fileName) {
        log.info("Generating upload url for file {}", fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsS3Property.bucketName())
                .key(fileName)
                .contentType(CONTENT_TYPE)
                .build();

        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(SIGNATURE_DURATION)
                .putObjectRequest(putObjectRequest)
                .build();

        URL preSignedUrl = s3Presigner.presignPutObject(preSignRequest).url();
        log.info("Generated upload pre-signed URL: {}", preSignedUrl);

        return UploadUrlDto.builder()
                .uploadUrl(preSignedUrl.toString())
                .build();
    }

    public DownloadUrlDto generateDownloadUrl(String fileName) {
        log.info("Generating download url for file: {}", fileName);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(awsS3Property.bucketName())
                .key(fileName)
                .build();

        GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(SIGNATURE_DURATION)
                .getObjectRequest(getObjectRequest)
                .build();

        URL preSignedUrl = s3Presigner.presignGetObject(preSignRequest).url();
        log.info("Generated download pre-signed URL: {}", preSignedUrl);

        return DownloadUrlDto.builder()
                .downloadUrl(preSignedUrl.toString())
                .build();
    }

    @Override
    public void deleteFile(String fileName) {
        log.info("Deleting file from S3: {}", fileName);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(awsS3Property.bucketName())
                .key(fileName)
                .build());

        log.info("File deleted successfully");
    }
}
