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

    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
    private static final String ORDER_NOT_FOUND_MESSAGE = "Order not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String OUT_OF_STOCK_MESSAGE = "Product is out of stock";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    private static final String INVALID_ARGUMENT_MESSAGE = "Invalid argument";
    private static final String FIELD_REQUIRED_MESSAGE = "Field is required";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error";
    private static final String OBJECT_NAME = "object";
    private static final String FIELD_NAME = "field";
    private static final int HTTP_STATUS_404 = 404;
    private static final int HTTP_STATUS_400 = 400;
    private static final int HTTP_STATUS_401 = 401;
    private static final int HTTP_STATUS_500 = 500;

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleProductNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_404);
        assertThat(response.getBody().message()).isEqualTo(PRODUCT_NOT_FOUND_MESSAGE);
    }

    @Test
    void shouldHandleOrderNotFoundException() {
        OrderNotFoundException exception = new OrderNotFoundException(ORDER_NOT_FOUND_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOrderNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_404);
        assertThat(response.getBody().message()).isEqualTo(ORDER_NOT_FOUND_MESSAGE);
    }

    @Test
    void shouldHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException(USER_NOT_FOUND_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUserNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_404);
        assertThat(response.getBody().message()).isEqualTo(USER_NOT_FOUND_MESSAGE);
    }

    @Test
    void shouldHandleOutOfStockException() {
        OutOfStockException exception = new OutOfStockException(OUT_OF_STOCK_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOutOfStock(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_400);
        assertThat(response.getBody().message()).isEqualTo(OUT_OF_STOCK_MESSAGE);
    }

    @Test
    void shouldHandleUserAuthenticationException() {
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAuthentication(
            new org.springframework.security.authentication.BadCredentialsException(INVALID_CREDENTIALS_MESSAGE)
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_401);
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException(INVALID_ARGUMENT_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgument(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_400);
        assertThat(response.getBody().message()).isEqualTo(INVALID_ARGUMENT_MESSAGE);
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError(OBJECT_NAME, FIELD_NAME, FIELD_REQUIRED_MESSAGE);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<ValidationErrorResponse> response = exceptionHandler.handleValidationErrors(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_400);
        assertThat(response.getBody().message()).isEqualTo(VALIDATION_FAILED_MESSAGE);
        assertThat(response.getBody().errors()).containsKey(FIELD_NAME);
        assertThat(response.getBody().errors().get(FIELD_NAME)).isEqualTo(FIELD_REQUIRED_MESSAGE);
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception(UNEXPECTED_ERROR_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HTTP_STATUS_500);
        assertThat(response.getBody().message()).contains("An unexpected error occurred");
    }
}

