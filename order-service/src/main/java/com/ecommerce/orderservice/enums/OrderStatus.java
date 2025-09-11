package com.ecommerce.orderservice.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order lifecycle status")
public enum OrderStatus {

    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED

}
