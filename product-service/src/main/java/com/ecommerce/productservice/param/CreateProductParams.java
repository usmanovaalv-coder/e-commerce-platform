package com.ecommerce.productservice.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductParams {

    @NotEmpty
    String name;
    @Length(max = 1000)
    String description;
    @NotNull
    Double price;
    @NotEmpty
    String category;
    @NotNull
    Integer stockQuantity;
    String imageName;

}
