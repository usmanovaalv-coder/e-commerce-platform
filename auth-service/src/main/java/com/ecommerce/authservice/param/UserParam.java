package com.ecommerce.authservice.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserParam {

    @NotEmpty
    String userName;
    @NotEmpty
    String email;
    @NotEmpty
    String password;
}
