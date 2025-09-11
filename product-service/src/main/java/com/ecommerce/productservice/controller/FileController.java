package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.DownloadUrlDto;
import com.ecommerce.productservice.dto.UploadUrlDto;
import com.ecommerce.productservice.exception.ApiError;
import com.ecommerce.productservice.service.S3StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Files")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"UNAUTHORIZED","message":"JWT is missing or invalid","path":"/api/v1/files","timestamp":"2025-09-11T12:00:00Z"}
            """))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"FORBIDDEN","message":"Insufficient authority","path":"/api/v1/files","timestamp":"2025-09-11T12:00:00Z"}
            """))
        )
})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/files")
@PreAuthorize("hasAuthority('OPERATOR') or hasAnyAuthority('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    S3StorageService s3StorageService;

    @Operation(
            summary = "Generate pre-signed upload URL",
            description = "Returns a pre-signed URL for uploading a file from S3 (PUT). "
                    + "URL действует ограниченное время."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Upload URL generated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UploadUrlDto.class),
                            examples = @ExampleObject(value = """
                  {"uploadUrl":"https://bucket.s3.amazonaws.com/image.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&..."}
                """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file name",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping("/upload-url")
    public ResponseEntity<UploadUrlDto> uploadFile(@RequestParam("fileName") String fileName) {
        UploadUrlDto preSignedUrl = s3StorageService.generateUploadUrl(fileName);
        return ResponseEntity.ok(preSignedUrl);
    }

    @Operation(
            summary = "Generate pre-signed download URL",
            description = "Returns a pre-signed URL for downloading a file from S3 (GET). "
                    + "URL действует ограниченное время."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Download URL generated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DownloadUrlDto.class),
                            examples = @ExampleObject(value = """
                  {"downloadUrl":"https://bucket.s3.amazonaws.com/image.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&..."}
                """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file name",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "File not found (if the service verifies the existence of the key)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @GetMapping("/{fileName}/download-url")
    public ResponseEntity<DownloadUrlDto> getDownloadUrl(@PathVariable String fileName) {
        DownloadUrlDto preSignedUrl = s3StorageService.generateDownloadUrl(fileName);
        return ResponseEntity.ok(preSignedUrl);
    }

    @Operation(
            summary = "Delete file from S3",
            description = "Deletes an object from S3. The operation is idempotent: deleting non-existing key and return 204."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "File deleted"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file name",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(
            @Parameter(description = "File name (S3-key)", example = "image.jpg")
            @PathVariable String fileName) {
        s3StorageService.deleteFile(fileName);
        return ResponseEntity.noContent().build();
    }
}
