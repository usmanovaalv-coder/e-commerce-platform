package com.ecommerce.productservice.service;

import com.ecommerce.productservice.model.GetProductsResponse;
import com.ecommerce.productservice.model.ProductAvailabilityRequest;
import com.ecommerce.productservice.model.ProductAvailabilityResponse;

import java.util.List;

public interface ProductService {

    GetProductsResponse getProductsByIds(List<Long> ids);

    ProductAvailabilityResponse checkAvailability(ProductAvailabilityRequest request);
}
