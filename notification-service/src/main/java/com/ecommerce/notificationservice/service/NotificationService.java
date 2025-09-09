package com.ecommerce.notificationservice.service;

import com.ecommerce.notificationservice.model.NotificationRequest;

public interface NotificationService {

    void sendNotification(NotificationRequest request);

}
