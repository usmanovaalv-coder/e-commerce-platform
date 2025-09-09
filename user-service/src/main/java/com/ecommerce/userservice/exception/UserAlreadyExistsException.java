package com.ecommerce.userservice.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAlreadyExistsException extends RuntimeException {

    String username;

    public UserAlreadyExistsException(String username) {
        super(String.format("User with username '%s' already exists.", username));
        this.username = username;
    }

    public UserAlreadyExistsException(String message, String username) {
        super(message);
        this.username = username;
    }

}
