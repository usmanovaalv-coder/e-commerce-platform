package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.dto.UserDto;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.enums.NotificationType;
import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.mapper.OrderMapper;
import com.ecommerce.orderservice.model.NotificationRequest;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.KafkaNotificationProducer;
import com.ecommerce.orderservice.service.OrderManagementService;
import com.ecommerce.orderservice.service.ProductProcessingService;
import com.ecommerce.orderservice.service.UserProcessingService;
import com.ecommerce.orderservice.utils.OrderStatusUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderManagementServiceImpl implements OrderManagementService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;

    UserProcessingService userProcessingService;
    ProductProcessingService productProcessingService;

    KafkaNotificationProducer kafkaNotificationProducer;

    @Override
    @Transactional
    public OrderDto updateOrderStatus(long orderId, OrderStatus newStatus) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        UserDto userDto = userProcessingService.fetchClientData(orderEntity.getUserId());

        /*log.info("Attempting to update order {} from {} to {}", orderId, orderEntity.getStatus(), newStatus);

        OrderStatusUtils.validateStatusTransition(orderEntity.getStatus(), newStatus);

        if (newStatus == OrderStatus.PROCESSING) {
            productProcessingService.checkProductAvailability(orderEntity);
        }

        orderEntity.setStatus(newStatus);
        orderRepository.save(orderEntity);*/

        sendOrderStatusUpdate(orderEntity.getUserId(), orderId, newStatus);
        log.info("Order {} successfully updated to {}", orderId, newStatus);

        return orderMapper.toOrderDto(orderEntity, userDto);
    }

    private void sendOrderStatusUpdate(long userId, long orderId, OrderStatus newStatus) {
        NotificationRequest notification = NotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.EMAIL)
                .message( "Your order #" + orderId + " is now " + newStatus)
                .build();

        kafkaNotificationProducer.sendNotification(notification);
    }
}
