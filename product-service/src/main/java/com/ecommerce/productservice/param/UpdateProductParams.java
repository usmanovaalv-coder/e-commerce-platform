package com.ecommerce.productservice.param;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductParams {

    String name;

    String description;

    Double price;

    String category;

    Integer stockQuantity;

    String imageName;
}
