package com.ecommerce.orderservice.utils;

import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.exception.InvalidOrderStatusException;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;

@UtilityClass
public class OrderStatusUtils {

    public static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_ORDER_TRANSITIONS = getAllowedTransitions();

    public static void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        Set<OrderStatus> allowedTransitions = ALLOWED_ORDER_TRANSITIONS.getOrDefault(currentStatus, Set.of());

        if (!allowedTransitions.contains(newStatus)) {
            throw new InvalidOrderStatusException("Cannot change order status from " + currentStatus + " to " + newStatus);
        }
    }

    private Map<OrderStatus, Set<OrderStatus>> getAllowedTransitions() {
        return Map.of(
                OrderStatus.PENDING, Set.of(OrderStatus.PROCESSING, OrderStatus.CANCELED),
                OrderStatus.PROCESSING, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELED),
                OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED),
                OrderStatus.DELIVERED, Set.of(),
                OrderStatus.CANCELED, Set.of()
        );
    }
}
