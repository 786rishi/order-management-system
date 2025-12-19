package com.assignment.ordermanagement.shared.exception;

import com.assignment.ordermanagement.order.domain.exception.OrderNotFoundException;
import com.assignment.ordermanagement.order.domain.exception.OutOfStockException;
import com.assignment.ordermanagement.product.domain.exception.ProductNotFoundException;
import com.assignment.ordermanagement.user.domain.exception.UserAuthenticationException;
import com.assignment.ordermanagement.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException("Product not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleProductNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("Product not found");
    }

    @Test
    void shouldHandleOrderNotFoundException() {
        OrderNotFoundException exception = new OrderNotFoundException("Order not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOrderNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("Order not found");
    }

    @Test
    void shouldHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUserNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("User not found");
    }

    @Test
    void shouldHandleOutOfStockException() {
        OutOfStockException exception = new OutOfStockException("Product is out of stock");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOutOfStock(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message()).isEqualTo("Product is out of stock");
    }

    @Test
    void shouldHandleUserAuthenticationException() {
        UserAuthenticationException exception = new UserAuthenticationException("Invalid credentials");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAuthentication(
            new org.springframework.security.authentication.BadCredentialsException("Invalid credentials")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(401);
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgument(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message()).isEqualTo("Invalid argument");
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "Field is required");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<ValidationErrorResponse> response = exceptionHandler.handleValidationErrors(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message()).isEqualTo("Validation failed");
        assertThat(response.getBody().errors()).containsKey("field");
        assertThat(response.getBody().errors().get("field")).isEqualTo("Field is required");
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().message()).contains("An unexpected error occurred");
    }
}

