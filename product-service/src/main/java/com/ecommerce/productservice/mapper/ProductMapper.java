package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.entity.CategoryEntity;
import com.ecommerce.productservice.entity.ProductEntity;
import com.ecommerce.productservice.model.ProductResponse;
import com.ecommerce.productservice.param.CreateProductParams;
import com.ecommerce.productservice.param.UpdateProductParams;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

    @Mapping(target = "category", source = "category.name")
    ProductDto toProductDto(ProductEntity productEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "productParams.name")
    @Mapping(target = "description", source = "productParams.description")
    @Mapping(target = "price", source = "productParams.price")
    @Mapping(target = "stockQuantity", source = "productParams.stockQuantity")
    @Mapping(target = "imageName", source = "productParams.imageName")
    @Mapping(target = "category", source = "categoryEntity")
    ProductEntity toProductEntity(CreateProductParams productParams, CategoryEntity categoryEntity);

    @Mapping(target = "name", source = "productParams.name")
    @Mapping(target = "description", source = "productParams.description")
    @Mapping(target = "price", source = "productParams.price")
    @Mapping(target = "stockQuantity", source = "productParams.stockQuantity")
    @Mapping(target = "imageName", source = "productParams.imageName")
    @Mapping(target = "category", source = "categoryEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductEntity toProductEntityUpdate(@MappingTarget ProductEntity productEntity,
                                        UpdateProductParams productParams,
                                        CategoryEntity categoryEntity);

    @Mapping(target = "quantity", source = "stockQuantity")
    ProductResponse toProductResponse(ProductEntity productEntity);
}
