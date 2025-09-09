package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.enums.NotificationType;
import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.model.NotificationRequest;
import com.ecommerce.orderservice.service.KafkaNotificationProducer;
import com.ecommerce.orderservice.service.OrderManagementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("/api/v1/orders/manage")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderManagementController {

    OrderManagementService orderManagementService;
    KafkaNotificationProducer producer;

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable long orderId,
                                                      @RequestParam OrderStatus status) {
        OrderDto updatedOrder = orderManagementService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping
    public void sendNotification() {
        NotificationRequest notification = NotificationRequest.builder()
                .userId(2L)
                .type(NotificationType.EMAIL)
                .message( "Your order #" + 10 + " is now " + "PROCESSING")
                .build();

        producer.sendNotification(notification);
    }
}
