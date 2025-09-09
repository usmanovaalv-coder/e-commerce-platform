package com.ecommerce.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestcontainersDatabaseConfig {

    static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    private static final String DB_NAME = "test_db";
    private static final String USER_NAME = "test_user";
    private static final String PASSWORD = "test_password";

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName(DB_NAME)
                .withUsername(USER_NAME)
                .withPassword(PASSWORD)
                .withInitScript("schema.sql");
        POSTGRES_CONTAINER.start();

        System.setProperty("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRES_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", POSTGRES_CONTAINER.getPassword());
    }

}
