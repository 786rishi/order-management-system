package com.assignment.ordermanagement.user.domain.model;

/**
 * User Domain Model - Pure business entity
 */
public class User {
    private Long id;
    private String username;
    private String password;  // Encrypted
    private Role role;

    // Constructor for creating new users
    public User(String username, String password, Role role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor for reconstructing from persistence
    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Business methods
    public boolean isPremium() {
        return role == Role.PREMIUM_USER;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public void updatePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.password = newPassword;
    }

    public void upgradeToRole(Role newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = newRole;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    // For infrastructure to set ID
    public void setId(Long id) {
        this.id = id;
    }
}

