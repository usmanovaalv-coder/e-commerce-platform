package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.enums.OrderStatus;

public interface OrderManagementService {

    OrderDto updateOrderStatus(long orderId, OrderStatus status);
}
