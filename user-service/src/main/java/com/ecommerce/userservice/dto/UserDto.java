package com.ecommerce.userservice.dto;

import com.ecommerce.userservice.enums.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    long id;
    String userName;
    String email;
    String phone;
    String address;
    UserRole role;
}
