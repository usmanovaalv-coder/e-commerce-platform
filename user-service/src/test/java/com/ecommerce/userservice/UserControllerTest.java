package com.ecommerce.userservice;

import com.ecommerce.userservice.entity.UserEntity;
import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.exception.ApiError;
import com.ecommerce.userservice.model.CreateUserRequest;
import com.ecommerce.userservice.model.CreateUserResponse;
import com.ecommerce.userservice.model.GetUserResponse;
import com.ecommerce.userservice.util.UtilsTest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControllerTest extends BaseTest {

    private static final String USER_SOURCE = "src/test/resources/user/%s";

    private static final String GET_USER_BY_NAME_ENDPOINT = "/api/v1/users/%s";
    private static final String POST_CREATE_USER_ENDPOINT = "/api/v1/users";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testGetByUsernameSuccess() {
        UserEntity userEntity = prepareUser();
        GetUserResponse expectedResponse = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/getUserResponse.json"), GetUserResponse.class);

        ResponseEntity<GetUserResponse> actualResponse = mockMvcUtils.performGet(
                String.format(GET_USER_BY_NAME_ENDPOINT, userEntity.getUserName()), GetUserResponse.class);
        GetUserResponse body = actualResponse.getBody();

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(body).isNotNull().usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResponse);
        assertThat(body.getId()).isNotNull()
                .isEqualTo(userEntity.getId());

    }

    @Test
    public void testGetByUsernameError() {
        prepareUser();

        ResponseEntity<ApiError> actualResponse = mockMvcUtils.performGet(
                String.format(GET_USER_BY_NAME_ENDPOINT, ERROR_USER_NAME), ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());

        ApiError errorResponse = actualResponse.getBody();
        assertThat(errorResponse).isNotNull();

        assertEquals("A user not found.", errorResponse.getMessage());
        assertEquals("USER_NOT_FOUND", errorResponse.getCode());
        assertEquals(String.format("User with username '%s' not found", ERROR_USER_NAME), errorResponse.getDetails());
    }

    @Test
    public void testCreateUserSuccess() {
        CreateUserRequest createUserRequest = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/createUserRequest.json"), CreateUserRequest.class);

        ResponseEntity<CreateUserResponse> actualResponse = mockMvcUtils.performPost(
                POST_CREATE_USER_ENDPOINT, createUserRequest, CreateUserResponse.class);
        CreateUserResponse body = actualResponse.getBody();

        CreateUserResponse expectedResponse = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/createUserResponse.json"), CreateUserResponse.class);

        checkUserSavedInDatabase(createUserRequest, body.getId());

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(body).isNotNull().usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    public void testCreateUserError() {
        prepareUser();

        CreateUserRequest createUserRequest = UtilsTest.readAsObject(
                String.format(USER_SOURCE,  "/createUserRequest.json"), CreateUserRequest.class);

        ResponseEntity<ApiError> actualResponse = mockMvcUtils.performPost(
                POST_CREATE_USER_ENDPOINT, createUserRequest, ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());

        ApiError errorResponse = actualResponse.getBody();
        assertThat(errorResponse).isNotNull();

        assertEquals("A user with the same username or email already exists.", errorResponse.getMessage());
        assertEquals("USER_ALREADY_EXISTS", errorResponse.getCode());
        assertEquals(String.format("User with username '%s' already exists.", createUserRequest.getUserName()), errorResponse.getDetails());
    }

    private void checkUserSavedInDatabase(CreateUserRequest createUserRequest, Long responseId) {
        UserEntity savedUser = userRepository.findByUserName(createUserRequest.getUserName()).orElseThrow(
                () -> new AssertionError("User was not saved in the database")
        );

        assertThat(savedUser.getId()).isEqualTo(responseId);

        assertThat(savedUser.getPassword()).isNotEqualTo(createUserRequest.getPassword());
        assertThat(passwordEncoder.matches(createUserRequest.getPassword(), savedUser.getPassword())).isTrue();

        assertThat(savedUser.isBlocked()).isFalse();
        assertThat(savedUser.getRole()).isNotNull().isEqualTo(UserRole.USER);
    }

    private UserEntity prepareUser() {
        UserEntity userEntity = UtilsTest.readAsObject(String.format(USER_SOURCE,  "/userEntity.json"), UserEntity.class);
        return userRepository.save(userEntity);
    }
}
