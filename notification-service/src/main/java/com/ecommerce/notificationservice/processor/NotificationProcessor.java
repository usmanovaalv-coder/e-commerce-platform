package com.ecommerce.notificationservice.processor;

import com.ecommerce.notificationservice.model.NotificationRequest;

public interface NotificationProcessor {

    void process(NotificationRequest request);

}
