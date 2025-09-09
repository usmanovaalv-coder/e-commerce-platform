package com.ecommerce.notificationservice.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class FeignClientInterceptor implements RequestInterceptor {

    ServiceTokenProvider tokenProvider;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            String token = tokenProvider.getToken();
            requestTemplate.header("Authorization", "Bearer " + token);
            log.debug("Feign Request -> Added Authorization token");
        } catch (Exception e) {
            log.warn("Feign Request -> Failed to add Authorization token", e);
        }
    }
}
