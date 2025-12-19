package com.assignment.ordermanagement.user.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void shouldHaveUserRole() {
        assertThat(Role.USER).isNotNull();
    }

    @Test
    void shouldHavePremiumUserRole() {
        assertThat(Role.PREMIUM_USER).isNotNull();
    }

    @Test
    void shouldHaveAdminRole() {
        assertThat(Role.ADMIN).isNotNull();
    }

    @Test
    void shouldReturnCorrectRoleValues() {
        Role[] roles = Role.values();
        
        assertThat(roles).hasSize(3);
        assertThat(roles).contains(Role.USER, Role.PREMIUM_USER, Role.ADMIN);
    }

    @Test
    void shouldValueOfUser() {
        Role role = Role.valueOf("USER");
        
        assertThat(role).isEqualTo(Role.USER);
    }

    @Test
    void shouldValueOfPremiumUser() {
        Role role = Role.valueOf("PREMIUM_USER");
        
        assertThat(role).isEqualTo(Role.PREMIUM_USER);
    }

    @Test
    void shouldValueOfAdmin() {
        Role role = Role.valueOf("ADMIN");
        
        assertThat(role).isEqualTo(Role.ADMIN);
    }
}

