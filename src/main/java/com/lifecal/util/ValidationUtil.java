package com.lifecal.util;

/**
 * ValidationUtil - Utility class for input validation
 */
public class ValidationUtil {

    /**
     * Validate username (not empty, alphanumeric + underscore, length 3-20)
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * Validate password (minimum 4 characters)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }

    /**
     * Validate that a string represents a positive number
     */
    public static boolean isPositiveNumber(String value) {
        try {
            double num = Double.parseDouble(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate weight (between 20 and 500 kg)
     */
    public static boolean isValidWeight(double weight) {
        return weight >= 20 && weight <= 500;
    }

    /**
     * Validate height (between 50 and 300 cm)
     */
    public static boolean isValidHeight(double height) {
        return height >= 50 && height <= 300;
    }

    /**
     * Check if a string is null or empty
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validate email format (optional field - null/empty is valid)
     * Uses standard email regex pattern
     */
    public static boolean isValidEmail(String email) {
        // Email is optional, so null or empty is valid
        if (email == null || email.trim().isEmpty()) {
            return true;
        }

        // Basic email regex pattern
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
