package com.ecommerce.notificationservice.processor.impl;

import com.ecommerce.notificationservice.model.NotificationRequest;
import com.ecommerce.notificationservice.processor.NotificationProcessor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SmsNotificationProcessor implements NotificationProcessor {

    @Override
    public void process(NotificationRequest request) {

    }
}
