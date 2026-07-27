package com.anjaniy.spring_boot_caching_redis.controller;

import com.anjaniy.spring_boot_caching_redis.dto.ApiResponse;
import com.anjaniy.spring_boot_caching_redis.dto.ProductDto;
import com.anjaniy.spring_boot_caching_redis.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts() {
        return ResponseEntity.ok(ApiResponse.<List<ProductDto>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("Products fetched successfully!")
                .data(productService.getProducts())
                .build()
        );

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok(ApiResponse.<ProductDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("Product fetched successfully!")
                .data(productService.getProduct(id))
                .build()
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(
                ApiResponse.<ProductDto>builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.OK.value())
                        .message("Product updated successfully!")
                        .data(productService.updateProduct(productDto))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> deleteProduct(@PathVariable long id) {
        return ResponseEntity.ok(
                ApiResponse.<ProductDto>builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.OK.value())
                        .message("Product deleted successfully!")
                        .data(productService.deleteProduct(id))
                        .build()
        );
    }
}
