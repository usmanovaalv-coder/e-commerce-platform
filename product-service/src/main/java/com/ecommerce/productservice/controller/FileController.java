package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.DownloadUrlDto;
import com.ecommerce.productservice.dto.UploadUrlDto;
import com.ecommerce.productservice.service.S3StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/files")
@PreAuthorize("hasAuthority('OPERATOR') or hasAnyAuthority('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    S3StorageService s3StorageService;

    @PostMapping("/upload-url")
    public ResponseEntity<UploadUrlDto> uploadFile(@RequestParam("fileName") String fileName) {
        UploadUrlDto preSignedUrl = s3StorageService.generateUploadUrl(fileName);
        return ResponseEntity.ok(preSignedUrl);
    }

    @GetMapping("/{fileName}/download-url")
    public ResponseEntity<DownloadUrlDto> getDownloadUrl(@PathVariable String fileName) {
        DownloadUrlDto preSignedUrl = s3StorageService.generateDownloadUrl(fileName);
        return ResponseEntity.ok(preSignedUrl);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        s3StorageService.deleteFile(fileName);
        return ResponseEntity.noContent().build();
    }
}
