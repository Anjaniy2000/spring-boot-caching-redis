package com.anjaniy.spring_boot_caching_redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // enables caching in our application
public class SpringBootCachingRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCachingRedisApplication.class, args);
	}

}
