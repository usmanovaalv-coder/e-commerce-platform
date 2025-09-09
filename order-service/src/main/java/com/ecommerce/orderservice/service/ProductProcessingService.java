package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.ProductServiceClient;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.entity.OrderItemEntity;
import com.ecommerce.orderservice.model.GetProductsRequest;
import com.ecommerce.orderservice.model.GetProductsResponse;
import com.ecommerce.orderservice.model.ProductAvailabilityRequest;
import com.ecommerce.orderservice.model.ProductAvailabilityResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductProcessingService {

    ProductServiceClient productServiceClient;

    public GetProductsResponse getProducts(List<Long> productIds) {
        log.info("Fetching items for productIds: {}", productIds);

        GetProductsRequest request = new GetProductsRequest(productIds);
        GetProductsResponse productsResponse = productServiceClient.getProducts(request);

        log.info("Fetched items for productIds: {}, data: {}", productIds, productsResponse);

        return productsResponse;
    }

    public void checkProductAvailability(OrderEntity order) {
        log.info("Checking product availability for order {}", order.getId());

        Map<Long, Integer> productQuantities = order.getItems().stream()
                .collect(Collectors.toMap(OrderItemEntity::getProductId, OrderItemEntity::getQuantity));

        ProductAvailabilityResponse response = productServiceClient.checkAvailability(new ProductAvailabilityRequest(productQuantities));

        if (!response.isAvailable()) {
            throw new IllegalStateException("Insufficient stock for order " + order.getId());
        }

        log.info("All products are available for order {}", order.getId());
    }
}
