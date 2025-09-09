package com.ecommerce.productservice;

import com.ecommerce.productservice.util.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseTest extends ProductManagerServiceApplicationTests {

    static final String USERNAME = "testUser";
    static final String ADMIN_ROLE = "ADMIN";

    @Autowired
    protected MockMvcUtils mockMvcUtils;

}
