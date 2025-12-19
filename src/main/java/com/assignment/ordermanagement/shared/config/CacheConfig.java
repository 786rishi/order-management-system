package com.assignment.ordermanagement.shared.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Cache Configuration
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // Using default Spring Boot cache configuration
    // Can be extended with Redis or Caffeine if needed
}

