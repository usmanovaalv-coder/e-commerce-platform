package com.ecommerce.notificationservice.processor.impl;

import com.ecommerce.notificationservice.model.NotificationRequest;
import com.ecommerce.notificationservice.processor.NotificationProcessor;
import com.ecommerce.notificationservice.service.impl.EmailNotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailNotificationProcessor implements NotificationProcessor {

    EmailNotificationService emailNotificationService;

    @Override
    public void process(NotificationRequest request) {
        emailNotificationService.sendNotification(request);
    }
}
