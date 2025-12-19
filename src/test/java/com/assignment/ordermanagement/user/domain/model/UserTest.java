package com.assignment.ordermanagement.user.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void shouldCreateUserWithRequiredFields() {
        String username = "testuser";
        String password = "password";
        Role role = Role.USER;

        User user = new User(username, password, role);

        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    void shouldCreateUserWithAllFields() {
        Long id = 1L;
        String username = "testuser";
        String password = "password";
        Role role = Role.PREMIUM_USER;

        User user = new User(id, username, password, role);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        assertThatThrownBy(() -> new User(null, "password", Role.USER))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Username cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsEmpty() {
        assertThatThrownBy(() -> new User("", "password", Role.USER))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Username cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsBlank() {
        assertThatThrownBy(() -> new User("   ", "password", Role.USER))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Username cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        assertThatThrownBy(() -> new User("username", null, Role.USER))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty() {
        assertThatThrownBy(() -> new User("username", "", Role.USER))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsBlank() {
        assertThatThrownBy(() -> new User("username", "   ", Role.USER))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        assertThatThrownBy(() -> new User("username", "password", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Role cannot be null");
    }

    @Test
    void shouldReturnTrueForPremiumUser() {
        User user = new User("testuser", "password", Role.PREMIUM_USER);

        assertThat(user.isPremium()).isTrue();
    }

    @Test
    void shouldReturnFalseForRegularUser() {
        User user = new User("testuser", "password", Role.USER);

        assertThat(user.isPremium()).isFalse();
    }

    @Test
    void shouldReturnTrueForAdmin() {
        User user = new User("admin", "password", Role.ADMIN);

        assertThat(user.isAdmin()).isTrue();
    }

    @Test
    void shouldReturnFalseForNonAdmin() {
        User user = new User("testuser", "password", Role.USER);

        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    void shouldUpdatePassword() {
        User user = new User("testuser", "oldpassword", Role.USER);

        user.updatePassword("newpassword");

        assertThat(user.getPassword()).isEqualTo("newpassword");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPasswordToNull() {
        User user = new User("testuser", "password", Role.USER);

        assertThatThrownBy(() -> user.updatePassword(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPasswordToEmpty() {
        User user = new User("testuser", "password", Role.USER);

        assertThatThrownBy(() -> user.updatePassword(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password cannot be empty");
    }

    @Test
    void shouldUpgradeRole() {
        User user = new User("testuser", "password", Role.USER);

        user.upgradeToRole(Role.PREMIUM_USER);

        assertThat(user.getRole()).isEqualTo(Role.PREMIUM_USER);
    }

    @Test
    void shouldThrowExceptionWhenUpgradingToNullRole() {
        User user = new User("testuser", "password", Role.USER);

        assertThatThrownBy(() -> user.upgradeToRole(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Role cannot be null");
    }

    @Test
    void shouldSetId() {
        User user = new User("testuser", "password", Role.USER);

        user.setId(100L);

        assertThat(user.getId()).isEqualTo(100L);
    }
}

