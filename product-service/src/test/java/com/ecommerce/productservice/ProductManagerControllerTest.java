package com.ecommerce.productservice;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.entity.CategoryEntity;
import com.ecommerce.productservice.entity.ProductEntity;
import com.ecommerce.productservice.exception.ApiError;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.util.UtilsTest;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductManagerControllerTest extends BaseTest{

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    static String PRODUCT_SOURCE = "src/test/resources/product/%s";
    static String GET_PRODUCT_BY_ID_ENDPOINT = "/api/v1/products-manager/%s";
    static String GET_PRODUCTS_ENDPOINT = "/api/v1/products-manager";

    @BeforeEach
    public void cleanDatabase() {
        productRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = {ADMIN_ROLE})
    public void testGetProductByIdSuccess() {
        ProductEntity productEntity = prepareProduct();

        ResponseEntity<ProductDto> actualResponse = mockMvcUtils
                .performGet(String.format(GET_PRODUCT_BY_ID_ENDPOINT, productEntity.getId()), ProductDto.class);
        ProductDto body = actualResponse.getBody();

        ProductDto expectedResponse = UtilsTest.readAsObject(
                String.format(PRODUCT_SOURCE,  "productDto.json"), ProductDto.class);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(body).isNotNull().usingRecursiveComparison()
                .ignoringFields("id", "created", "updated")
                .isEqualTo(expectedResponse);
        assertThat(body.getId()).isNotNull().isEqualTo(productEntity.getId());
        assertThat(body.getCreated().getTime()).isCloseTo(productEntity.getCreated().getTime(), Offset.offset(5L));
        assertThat(body.getUpdated().getTime()).isCloseTo(productEntity.getUpdated().getTime(), Offset.offset(5L));
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = {ADMIN_ROLE})
    public void testGetProductByIdError() {
        ResponseEntity<ApiError> actualResponse = mockMvcUtils
                .performGet(String.format(GET_PRODUCT_BY_ID_ENDPOINT, 1L), ApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());

        ApiError errorResponse = actualResponse.getBody();
        assertThat(errorResponse).isNotNull();

        assertEquals("An element not found.", errorResponse.getMessage());
        assertEquals("NOT_FOUND", errorResponse.getCode());
        assertEquals(String.format("Product with id '%s' not found", 1L), errorResponse.getDetails());
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = {ADMIN_ROLE})
    public void testGetProductsSuccess() {
        PageImpl<ProductEntity> prepareProducts = new PageImpl<>(prepareProducts());

        List<ProductDto> expectedResponse = UtilsTest.readAsObject(
                String.format(PRODUCT_SOURCE,  "productDtos.json"), new TypeReference<>() {});

        ResponseEntity<PageImpl<ProductDto>> actualResponse = mockMvcUtils
                .performGet(GET_PRODUCTS_ENDPOINT, new TypeReference<PageImpl<ProductDto>>() {});

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertThat(actualResponse.getBody())
                .isNotNull()
                .satisfies(page -> {
                    assertThat(page.getContent()).isNotNull();
                });
    }

    private ProductEntity prepareProduct() {
        CategoryEntity categoryEntity = prepareCategory();
        ProductEntity productEntity = UtilsTest.readAsObject(
                String.format(PRODUCT_SOURCE,  "productEntity.json"), ProductEntity.class);
        productEntity.setCategory(categoryEntity);

        return productRepository.save(productEntity);
    }

    private List<ProductEntity> prepareProducts() {
        CategoryEntity category = prepareCategory();
        List<ProductEntity> products = UtilsTest.readAsObject(
                String.format(PRODUCT_SOURCE,  "productEntities.json"), new TypeReference<>() {});

        products.forEach(product -> product.setCategory(category));

        return productRepository.saveAll(products);
    }

    private CategoryEntity prepareCategory() {
        CategoryEntity categoryEntity = UtilsTest.readAsObject(
                String.format(PRODUCT_SOURCE,  "categoryEntity.json"), CategoryEntity.class
        );
        return categoryRepository.save(categoryEntity);
    }
}
