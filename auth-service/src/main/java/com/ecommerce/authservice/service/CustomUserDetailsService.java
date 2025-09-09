package com.ecommerce.authservice.service;

import com.ecommerce.authservice.client.UserServiceClient;
import com.ecommerce.authservice.model.GetUserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {

    UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        GetUserResponse userResponse = userServiceClient.getUser(username);

        if (userResponse == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return User.builder()
                .username(userResponse.getUserName())
                .password(userResponse.getPassword())
                .roles(userResponse.getRole().name())
                .build();
    }
}

