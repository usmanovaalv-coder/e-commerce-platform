package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.DownloadUrlDto;
import com.ecommerce.productservice.dto.UploadUrlDto;

public interface S3StorageService {

    UploadUrlDto generateUploadUrl(String fileName);

    DownloadUrlDto generateDownloadUrl(String fileName);

    void deleteFile(String fileName);
}
