package com.ecommerce.notificationservice.service.impl;

import com.ecommerce.notificationservice.client.UserServiceClient;
import com.ecommerce.notificationservice.model.ClientResponse;
import com.ecommerce.notificationservice.model.NotificationRequest;
import com.ecommerce.notificationservice.service.EmailSender;
import com.ecommerce.notificationservice.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailNotificationService implements NotificationService {

    UserServiceClient userServiceClient;
    EmailSender emailSender;

    @Override
    public void sendNotification(NotificationRequest request) {
        log.info("Sending email notification to user {}", request.getUserId());
        ClientResponse client = userServiceClient.getClientById(request.getUserId());

        if (client.getEmail() == null || client.getEmail().isEmpty()) {
            log.warn("The user {} doesn't have an email, the notification has not been sent", client.getId());
            return;
        }

        String subject = request.getSubject() != null ? request.getSubject() : "The notification";
        String message = "Hello, " + client.getName() + "!\n\n" + request.getMessage();

        emailSender.sendEmail(client.getEmail(), subject, message);
        log.info("📧 Email sent to the user {} ({})", client.getId(), client.getEmail());
    }
}
