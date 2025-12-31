package com.lifecal.common;

import javafx.scene.Scene;

/**
 * ThemeManager - Manages application theme switching between light and dark
 * modes
 */
public class ThemeManager {
    private static final String LIGHT_THEME = "/css/light-theme.css";
    private static final String DARK_THEME = "/css/dark-theme.css";
    private static boolean isDarkMode = false;

    /**
     * Toggle between light and dark themes for the given scene
     */
    public static void toggleTheme(Scene scene) {
        isDarkMode = !isDarkMode;
        applyTheme(scene);
    }

    /**
     * Apply current theme to scene
     */
    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        String themeFile = isDarkMode ? DARK_THEME : LIGHT_THEME;
        scene.getStylesheets().add(ThemeManager.class.getResource(themeFile).toExternalForm());
    }

    /**
     * Check if dark mode is currently active
     */
    public static boolean isDarkMode() {
        return isDarkMode;
    }

    /**
     * Set dark mode explicitly
     */
    public static void setDarkMode(boolean darkMode, Scene scene) {
        isDarkMode = darkMode;
        applyTheme(scene);
    }
}
