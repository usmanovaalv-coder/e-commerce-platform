package com.ecommerce.userservice;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.entity.UserEntity;
import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.exception.ApiError;
import com.ecommerce.userservice.util.UtilsTest;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserManagerControllerTest extends BaseTest{
    
    private static final String USER_SOURCE = "src/test/resources/usermanager/%s";
    private static final String GET_USER_BY_NAME_ENDPOINT = "/api/v1/users-manager/%s";
    private static final String GET_USERS_ENDPOINT = "/api/v1/users-manager";

    private static final String ERROR_USER_NAME = "errorUserName";

    @Test
    @WithMockUser(username = "testUser", authorities = {ADMIN_ROLE})
    public void testGetUserByUsernameSuccess() {
        UserEntity userEntity = prepareUser();

        ResponseEntity<UserDto> actualResponse = mockMvcUtils
                .performGet(String.format(GET_USER_BY_NAME_ENDPOINT, userEntity.getUserName()), UserDto.class);
        UserDto body = actualResponse.getBody();

        UserDto expectedResponse = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/userDto.json"), UserDto.class);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(body).isNotNull().usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResponse);
        assertThat(body.getId()).isNotNull()
                .isEqualTo(userEntity.getId());
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {ADMIN_ROLE})
    public void testGetUserByUsernameError() {
        ResponseEntity<ApiError> actualResponse = mockMvcUtils
                .performGet(String.format(GET_USER_BY_NAME_ENDPOINT, ERROR_USER_NAME), ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());

        ApiError errorResponse = actualResponse.getBody();
        assertThat(errorResponse).isNotNull();

        assertEquals("A user not found.", errorResponse.getMessage());
        assertEquals("USER_NOT_FOUND", errorResponse.getCode());
        assertEquals(String.format("User with username '%s' not found", ERROR_USER_NAME), errorResponse.getDetails());
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {ERROR_ROLE})
    public void testGetUserByUsernameAuthError() {
        ResponseEntity<ApiError> actualResponse = mockMvcUtils
                .performGet(String.format(GET_USER_BY_NAME_ENDPOINT, ERROR_USER_NAME), ApiError.class);

        assertEquals(HttpStatus.FORBIDDEN, actualResponse.getStatusCode());
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {ADMIN_ROLE})
    public void testGetUsersSuccess() {
        prepareUsers();
        List<UserDto> expectedResponse = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/userDtos.json"), new TypeReference<>() {});

        ResponseEntity<List<UserDto>> actualResponse = mockMvcUtils
                .performGet(GET_USERS_ENDPOINT, new TypeReference<>() {});

        List<UserDto> body = actualResponse.getBody();

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(body).isNotNull();
        assertThat(body.size()).isEqualTo(expectedResponse.size());

    }

    private UserEntity prepareUser() {
        UserEntity userEntity = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/userEntity.json"), UserEntity.class);
        return userRepository.save(userEntity);
    }

    private List<UserEntity> prepareUsers() {
        List<UserEntity> userEntities = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/userEntities.json"), new TypeReference<>() {});
        return userRepository.saveAll(userEntities);
    }
}
