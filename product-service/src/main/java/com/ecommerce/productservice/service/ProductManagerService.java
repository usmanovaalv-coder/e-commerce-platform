package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.model.GetProductsResponse;
import com.ecommerce.productservice.param.CreateProductParams;
import com.ecommerce.productservice.param.ProductParams;
import com.ecommerce.productservice.param.UpdateProductParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductManagerService {

    ProductDto getProductById(long id);

    Page<ProductDto> getProducts(ProductParams productParams, Pageable pageable);

    ProductDto createProduct(CreateProductParams productParams);

    ProductDto updateProduct(long id, UpdateProductParams productParams);

    void softDeleteUser(long id);

}
