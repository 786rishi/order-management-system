package com.assignment.ordermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Features:
 * - Order Management
 * - Order Processing with Discount Strategies
 * - User Authentication with JWT
 */
@SpringBootApplication
public class OrderManagement {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagement.class, args);
	}

}
