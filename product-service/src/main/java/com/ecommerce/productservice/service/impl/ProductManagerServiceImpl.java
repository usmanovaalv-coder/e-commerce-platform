package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.entity.CategoryEntity;
import com.ecommerce.productservice.entity.ProductEntity;
import com.ecommerce.productservice.mapper.ProductMapper;
import com.ecommerce.productservice.param.CreateProductParams;
import com.ecommerce.productservice.param.ProductParams;
import com.ecommerce.productservice.param.UpdateProductParams;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductManagerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductManagerServiceImpl implements ProductManagerService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;


    @Override
    public ProductDto getProductById(long id) {
        return productRepository.findById(id).map(productMapper::toProductDto)
                .orElseThrow(() -> new NoSuchElementException("Product with id '" + id + "' not found"));
    }

    @Override
    public Page<ProductDto> getProducts(ProductParams productParams, Pageable pageable) {
        return productRepository.findByFilters(
                        productParams.getName(),
                        productParams.getCategory(),
                        productParams.getMinPrice(),
                        productParams.getMaxPrice(), pageable)
                .map(productMapper::toProductDto);
    }

    @Override
    public ProductDto createProduct(CreateProductParams productParams) {
        CategoryEntity categoryEntity = findCategoryByName(productParams.getCategory());
        ProductEntity productEntity = productMapper.toProductEntity(productParams, categoryEntity);
        productEntity = productRepository.save(productEntity);

        log.info("Product with name '{}' created with id '{}'", productParams.getName(), productEntity.getId());
        return productMapper.toProductDto(productEntity);
    }

    @Override
    public ProductDto updateProduct(long id, UpdateProductParams productParams) {
        CategoryEntity categoryEntity = findCategoryByName(productParams.getCategory());
            return productRepository.findById(id)
                    .map(existing -> productMapper.toProductEntityUpdate(existing, productParams, categoryEntity))
                    .map(updatedProduct -> {
                        ProductDto dto = productMapper.toProductDto(productRepository.save(updatedProduct));
                        log.info("Product with id = {} has been updated successfully", id);
                        return dto;
                    })
                    .orElseThrow(() ->  new NoSuchElementException("Product with id '" + id + "' not found"));

    }

    @Override
    public void softDeleteUser(long id) {
        productRepository.findById(id).map(entity -> {
                    entity.setDeleted(true);
                    ProductEntity productEntity = productRepository.save(entity);
                    log.info("Product with id = {} has been deleted successfully", id);
                    return productEntity;
                })
                .orElseThrow(() -> new NoSuchElementException("Product with id '" + id + "' not found"));
    }

    private CategoryEntity findCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new UsernameNotFoundException("Category with name '" + name + "' not found"));

    }
}
