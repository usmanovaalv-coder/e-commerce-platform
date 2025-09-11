package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.exception.ApiError;
import com.ecommerce.productservice.model.GetProductsRequest;
import com.ecommerce.productservice.model.GetProductsResponse;
import com.ecommerce.productservice.model.ProductAvailabilityRequest;
import com.ecommerce.productservice.model.ProductAvailabilityResponse;
import com.ecommerce.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Products")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class)))
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @Operation(
            summary = "Get products by IDs (batch)",
            description = "Returns product details for a list of IDs. Requires role CLIENT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products resolved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProductsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/batch")
    public ResponseEntity<GetProductsResponse> getProductById(@RequestBody @Valid GetProductsRequest request) {
        GetProductsResponse getProductsResponse = productService.getProductsByIds(request.getProductIds());;
        return ResponseEntity.ok(getProductsResponse);
    }

    @Operation(
            summary = "Check products availability",
            description = "Checks stock availability for requested items. Requires role MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability calculated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAvailabilityResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "One or more products not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/check-availability")
    public ResponseEntity<ProductAvailabilityResponse> checkAvailability(@RequestBody @Valid ProductAvailabilityRequest request) {
        ProductAvailabilityResponse response = productService.checkAvailability(request);
        return ResponseEntity.ok(response);
    }
}
