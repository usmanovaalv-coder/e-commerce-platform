package com.ecommerce.notificationservice.client;

import feign.Headers;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "${auth.server-url}")
public interface AuthServiceClient {

    @PostMapping(value = "/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getToken(@RequestHeader("Authorization") String basicAuthHeader,
                           @RequestBody String body);

    @Getter
    @Setter
    class TokenResponse {
        private String access_token;
        private String token_type;
        private Integer expires_in;
        private String scope;
    }
}
