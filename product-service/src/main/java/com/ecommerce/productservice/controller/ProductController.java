package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.model.GetProductsRequest;
import com.ecommerce.productservice.model.GetProductsResponse;
import com.ecommerce.productservice.model.ProductAvailabilityRequest;
import com.ecommerce.productservice.model.ProductAvailabilityResponse;
import com.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/batch")
    public ResponseEntity<GetProductsResponse> getProductById(@RequestBody @Valid GetProductsRequest request) {
        GetProductsResponse getProductsResponse = productService.getProductsByIds(request.getProductIds());;
        return ResponseEntity.ok(getProductsResponse);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/check-availability")
    public ResponseEntity<ProductAvailabilityResponse> checkAvailability(@RequestBody @Valid ProductAvailabilityRequest request) {
        ProductAvailabilityResponse response = productService.checkAvailability(request);
        return ResponseEntity.ok(response);
    }
}
