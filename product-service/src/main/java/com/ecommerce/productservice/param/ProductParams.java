package com.ecommerce.productservice.param;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductParams {

    String name;
    String category;
    Double minPrice;
    Double maxPrice;

}
