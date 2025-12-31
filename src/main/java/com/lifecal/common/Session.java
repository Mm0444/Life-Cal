package com.lifecal.common;

import com.lifecal.model.User;

/**
 * Session - Singleton class to store the currently logged-in user
 * Provides global access to user data throughout the application
 */
public class Session {
    private static Session instance;
    private User currentUser;

    /**
     * Private constructor for singleton pattern
     */
    private Session() {
    }

    /**
     * Get singleton instance
     */
    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Set the currently logged-in user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Get the currently logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Clear session (logout)
     */
    public void clear() {
        this.currentUser = null;
    }

    /**
     * Check if a user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
