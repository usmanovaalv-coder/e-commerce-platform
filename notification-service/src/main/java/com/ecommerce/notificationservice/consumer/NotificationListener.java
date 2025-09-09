package com.ecommerce.notificationservice.consumer;

import com.ecommerce.notificationservice.enums.NotificationType;
import com.ecommerce.notificationservice.model.NotificationRequest;
import com.ecommerce.notificationservice.processor.NotificationProcessor;
import com.ecommerce.notificationservice.processor.impl.EmailNotificationProcessor;
import com.ecommerce.notificationservice.processor.impl.SmsNotificationProcessor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationListener {

    Map<NotificationType, NotificationProcessor> processors;

    @Autowired
    public NotificationListener(List<NotificationProcessor> processorList) {
        this.processors = processorList.stream()
                .collect(Collectors.toMap(this::getProcessorType, Function.identity()));
    }

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void listenNotifications(NotificationRequest request) {
        log.info("Received notification: {}", request);

        NotificationProcessor processor = processors.get(request.getType());
        if (processor != null) {
            processor.process(request);
        } else {
            log.warn("Unknown notification type: {}", request.getType());
        }
    }

    private NotificationType getProcessorType(NotificationProcessor processor) {
        if (processor instanceof EmailNotificationProcessor) {
            return NotificationType.EMAIL;
        } else if (processor instanceof SmsNotificationProcessor) {
            return NotificationType.SMS;
        }
        throw new IllegalArgumentException("Unknown processor type: " + processor.getClass());
    }
}
