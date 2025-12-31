package com.lifecal.service;

import com.lifecal.model.User;
import com.lifecal.repository.UserRepository;

/**
 * UserService - Handles user profile management
 */
public class UserService {
    private UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Update user profile
     */
    public boolean updateProfile(User user) {
        return userRepository.update(user);
    }

    /**
     * Get user by ID
     */
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
