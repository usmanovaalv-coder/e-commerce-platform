package com.ecommerce.notificationservice.model;

import com.ecommerce.notificationservice.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {

    long userId;
    NotificationType type;
    String subject;
    String message;

}
