package com.assignment.ordermanagement;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderManagementApplicationTest {

    @Test
    void shouldHaveMainMethod() throws NoSuchMethodException {
        assertThat(OrderManagement.class.getMethod("main", String[].class)).isNotNull();
    }

    @Test
    void shouldBeAnnotatedWithSpringBootApplication() {
        assertThat(OrderManagement.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class
        )).isTrue();
    }
}

