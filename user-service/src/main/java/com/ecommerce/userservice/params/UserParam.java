package com.ecommerce.userservice.params;

import com.ecommerce.userservice.enums.UserRole;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserParam {

    String userName;
    String email;
    String password;
    @Pattern(regexp = "^\\+7\\d{10}$")
    String phone;
    String address;
    UserRole role;
}
