package com.anjaniy.spring_boot_caching_redis.repository;

import com.anjaniy.spring_boot_caching_redis.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
