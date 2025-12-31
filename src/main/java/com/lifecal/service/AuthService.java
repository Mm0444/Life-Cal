package com.lifecal.service;

import com.lifecal.model.User;
import com.lifecal.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

/**
 * AuthService - Handles user authentication and registration
 */
public class AuthService {
    private UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Register a new user
     * 
     * @param user     User object with details
     * @param password Plain text password
     * @return true if registration successful
     */
    public boolean register(User user, String password) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }

        // Hash password using BCrypt
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPasswordHash(passwordHash);

        // Save user to database
        return userRepository.save(user);
    }

    /**
     * Login user
     * 
     * @param username Username
     * @param password Plain text password
     * @return User object if login successful, null otherwise
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return null;
        }

        // Verify password
        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }

        return null;
    }

    /**
     * Check if username is available
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Check if email is already taken
     */
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
}
