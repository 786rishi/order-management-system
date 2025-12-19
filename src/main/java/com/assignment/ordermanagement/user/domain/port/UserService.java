package com.assignment.ordermanagement.user.domain.port;

import com.assignment.ordermanagement.user.domain.model.User;

/**
 * Port (Interface) for User Domain Service
 */
public interface UserService {

    User getUserByUsername(String username);

}

