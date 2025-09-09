package com.ecommerce.userservice.model;

import com.ecommerce.userservice.enums.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetUserResponse {

    long id;
    String userName;
    String email;
    String password;
    UserRole role;

}
