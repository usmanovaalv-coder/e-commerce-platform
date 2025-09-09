package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.param.CreateOrderParams;

public interface OrderService {

    OrderDto createOrder(CreateOrderParams params, String username);

    void cancelOrder(long id);
}
