package com.ecommerce.notificationservice.security;

import com.ecommerce.notificationservice.client.AuthServiceClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceTokenProvider {

    final AuthServiceClient authServiceClient;

    @Value("${auth.client-id}")
    String clientId;
    @Value("${auth.client-secret}")
    String clientSecret;

    String cachedToken;
    Instant tokenExpiry = Instant.EPOCH;

    public synchronized String getToken() {
        if (cachedToken == null || Instant.now().isAfter(tokenExpiry.minusSeconds(60))) {
            String basicAuth = "Basic " + Base64.getEncoder()
                    .encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

            String body = "grant_type=client_credentials";

            AuthServiceClient.TokenResponse response = authServiceClient.getToken(basicAuth, body);

            if (response == null || response.getAccess_token() == null) {
                throw new IllegalStateException("Failed to obtain access token");
            }

            tokenExpiry = Instant.now().plusSeconds(response.getExpires_in());
            cachedToken = response.getAccess_token();
        }
        return cachedToken;
    }
}
