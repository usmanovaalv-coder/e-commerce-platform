package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.model.GetProductsRequest;
import com.ecommerce.orderservice.model.GetProductsResponse;
import com.ecommerce.orderservice.model.ProductAvailabilityRequest;
import com.ecommerce.orderservice.model.ProductAvailabilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", path = "/api/v1/products")
public interface ProductServiceClient {

    @PostMapping("/batch")
    GetProductsResponse getProducts(@RequestBody GetProductsRequest getProductsRequest);

    @PostMapping("/check-availability")
    ProductAvailabilityResponse checkAvailability(@RequestBody ProductAvailabilityRequest request);


}
