package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.entity.ProductEntity;
import com.ecommerce.productservice.mapper.ProductMapper;
import com.ecommerce.productservice.model.GetProductsResponse;
import com.ecommerce.productservice.model.ProductAvailabilityRequest;
import com.ecommerce.productservice.model.ProductAvailabilityResponse;
import com.ecommerce.productservice.model.ProductResponse;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    public GetProductsResponse getProductsByIds(List<Long> ids) {
        List<ProductResponse> products = productRepository.findAllById(ids).stream()
                .map(productMapper::toProductResponse)
                .toList();
        if (products.isEmpty()) {
            log.error("No products found for IDs: {}", ids);
            throw new NoSuchElementException("No products found for given IDs");
        }
        return GetProductsResponse.builder()
                .products(products)
                .build();
    }

    @Override
    public ProductAvailabilityResponse checkAvailability(ProductAvailabilityRequest request) {
        Map<Long, Integer> productQuantity = request.getProductQuantities();
        Set<Long> productIds = productQuantity.keySet();

        log.info("Checking availability for products: {}", productIds);

        List<ProductEntity> products = productRepository.findAllById(productIds);
        boolean isAvailable = products.stream()
                .noneMatch(product -> productQuantity.get(product.getId()) > product.getStockQuantity());

        return ProductAvailabilityResponse.builder()
                .available(isAvailable)
                .build();
    }
}
