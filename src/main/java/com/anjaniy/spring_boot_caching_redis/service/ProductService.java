package com.anjaniy.spring_boot_caching_redis.service;

import com.anjaniy.spring_boot_caching_redis.dto.ProductDto;
import com.anjaniy.spring_boot_caching_redis.entity.Product;
import com.anjaniy.spring_boot_caching_redis.exception.BadRequestException;
import com.anjaniy.spring_boot_caching_redis.exception.ResourceNotFoundException;
import com.anjaniy.spring_boot_caching_redis.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(value = "productList")
    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream().map(product -> ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build()).toList();
    }

//    @Cacheable(cacheNames = "products", key = "#id") // cache-names
    @Cacheable(value = "products", key = "#id") // value, both are same
    public ProductDto getProduct(long id) {
        if (id <= 0) {
            throw new BadRequestException("Product ID must be a positive number!");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id + "!"));

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    @Transactional
//    @CachePut(value = "products", key = "#productDto.id") // request productDto
//    @CachePut(value = "products", key = "#result.id") // returned productDto

    // simple + clean
    @Caching(
            put = {
                    @CachePut(value = "products", key = "#productDto.id")
            },
            evict = {
                    @CacheEvict(value = "productList", allEntries = true)
            }
    )

    // both are allowed
//    @CachePut(value = "products", key = "#productDto.id")
//    @CacheEvict(value = "productsList", allEntries = true)
    public ProductDto updateProduct(ProductDto productDto) {
        if (Objects.isNull(productDto.getId()) || productDto.getId() <= 0) {
            throw new BadRequestException("Product ID must be a positive number!");
        }

        long productId = productDto.getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId + "!"));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        Product updatedProduct = productRepository.save(product);

        return ProductDto.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .stock(updatedProduct.getStock())
                .build();
    }

    @Transactional
//    @CacheEvict(value = "products", key = "#id")
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "productList", allEntries = true)
    })
    public ProductDto deleteProduct(long id) {

        if (id <= 0) {
            throw new BadRequestException("Product ID must be a positive number!");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + id + "!"));

        productRepository.delete(product);

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
