package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.exception.ApiError;
import com.ecommerce.productservice.param.CreateProductParams;
import com.ecommerce.productservice.param.ProductParams;
import com.ecommerce.productservice.param.UpdateProductParams;
import com.ecommerce.productservice.service.ProductManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Products")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
})
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('OPERATOR') or hasAnyAuthority('ADMIN')")
@RequestMapping(value = "/api/v1/products-manager")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductManagerController {

    ProductManagerService productManagerService;

    @Operation(
            summary = "Get product by ID",
            description = "Returns product details by its identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable long id) {
        ProductDto productDto = productManagerService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }

    @Operation(
            summary = "Search products",
            description = "Returns a paginated list of products with filtering and sorting."
    )
    @Parameters({
            @Parameter(name = "page", description = "Page index (0..N)"),
            @Parameter(name = "size", description = "Page size"),
            @Parameter(name = "sort", description = "Sort, e.g. `created,desc`")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of products",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class))))
    })
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @ParameterObject @ModelAttribute ProductParams productParams,
            @ParameterObject
            @PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProductDto> products = productManagerService.getProducts(productParams, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Create product", description = "Creates a new product.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid CreateProductParams productParams) {
        ProductDto productDto = productManagerService.createProduct(productParams);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @Operation(
            summary = "Update product",
            description = "Updates product fields by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable long id,
                                              @RequestBody @Valid UpdateProductParams productParams) {
        ProductDto productDto = productManagerService.updateProduct(id, productParams);
        return ResponseEntity.ok(productDto);
    }

    @Operation(
            summary = "Soft delete product",
            description = "Marks product as deleted (soft delete)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productManagerService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
