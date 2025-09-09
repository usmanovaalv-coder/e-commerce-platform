package com.ecommerce.productservice;

import com.ecommerce.productservice.dto.DownloadUrlDto;
import com.ecommerce.productservice.dto.UploadUrlDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileControllerTest extends BaseTest{
    @MockBean
    S3Presigner s3Presigner;

    static String TEST_FILE_NAME = "test-file";
    static String TEST_PRE_SIGNED_URL = "https://mock-s3.com/pre-signed-url/%s";

    static String POST_UPLOAD_URL_ENDPOINT = "/api/v1/files/upload-url?fileName=%s";
    static String GET_DOWNLOAD_URL_ENDPOINT = "/api/v1/files/%s/download-url";

    @Test
    @WithMockUser(username = "testUser", authorities = {ADMIN_ROLE})
    public void testGetUploadUrlSuccess() throws Exception {
        UploadUrlDto expectedResponse = UploadUrlDto.builder()
                .uploadUrl(String.format(TEST_PRE_SIGNED_URL, TEST_FILE_NAME))
                .build();

        PresignedPutObjectRequest preSignedRequest = mock(PresignedPutObjectRequest.class);
        when(preSignedRequest.url()).thenReturn(new URL(String.format(TEST_PRE_SIGNED_URL, TEST_FILE_NAME)));
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenReturn(preSignedRequest);

        ResponseEntity<UploadUrlDto> actualResponse = mockMvcUtils
                .performPost(String.format(POST_UPLOAD_URL_ENDPOINT, TEST_FILE_NAME), null, UploadUrlDto.class);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(actualResponse.getBody()).isNotNull()
                .isEqualTo(expectedResponse);

        verify(s3Presigner, times(1)).presignPutObject(any(PutObjectPresignRequest.class));
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {ADMIN_ROLE})
    public void testGetDownloadUrlSuccess() throws Exception {
        DownloadUrlDto expectedResponse = DownloadUrlDto.builder()
                .downloadUrl(String.format(TEST_PRE_SIGNED_URL, TEST_FILE_NAME))
                .build();

        PresignedGetObjectRequest preSignedRequest = mock(PresignedGetObjectRequest.class);
        when(preSignedRequest.url()).thenReturn(new URL(String.format(TEST_PRE_SIGNED_URL, TEST_FILE_NAME)));
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(preSignedRequest);

        ResponseEntity<DownloadUrlDto> actualResponse = mockMvcUtils
                .performGet(String.format(GET_DOWNLOAD_URL_ENDPOINT, TEST_FILE_NAME), DownloadUrlDto.class);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(actualResponse.getBody()).isNotNull()
                .isEqualTo(expectedResponse);

        verify(s3Presigner, times(1)).presignGetObject(any(GetObjectPresignRequest.class));
    }

}
