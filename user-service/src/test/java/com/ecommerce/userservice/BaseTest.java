package com.ecommerce.userservice;

import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.util.MockMvcUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseTest extends UserApplicationTests {

    static final String ADMIN_ROLE = "ADMIN"    ;
    static final String ERROR_ROLE = "ERROR";
    static final String ERROR_USER_NAME = "errorUserName";

    @Autowired
    protected MockMvcUtils mockMvcUtils;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void cleanDatabase() {
        userRepository.deleteAll();
    }
}
