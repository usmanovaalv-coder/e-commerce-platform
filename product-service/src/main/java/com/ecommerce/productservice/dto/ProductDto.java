package com.ecommerce.productservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {

    long id;

    String name;

    String description;

    Double price;

    String category;

    Integer stockQuantity;

    String imageName;

    Timestamp created;

    Timestamp updated;
}
