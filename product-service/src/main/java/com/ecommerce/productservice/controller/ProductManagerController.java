package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.param.CreateProductParams;
import com.ecommerce.productservice.param.ProductParams;
import com.ecommerce.productservice.param.UpdateProductParams;
import com.ecommerce.productservice.service.ProductManagerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('OPERATOR') or hasAnyAuthority(('ADMIN'))")
@RequestMapping(value = "/api/v1/products-manager")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductManagerController {

    ProductManagerService productManagerService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable long id) {
        ProductDto productDto = productManagerService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @ModelAttribute ProductParams productParams,
            @PageableDefault(size = 10, sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProductDto> products = productManagerService.getProducts(productParams, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid CreateProductParams productParams) {
        ProductDto productDto = productManagerService.createProduct(productParams);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable long id,
                                              @RequestBody UpdateProductParams productParams) {
        ProductDto productDto = productManagerService.updateProduct(id, productParams);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productManagerService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
