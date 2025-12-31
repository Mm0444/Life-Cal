package com.lifecal.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * SceneManager - Handles navigation between different scenes/screens
 * Centralized scene switching for the application
 */
public class SceneManager {
    private static Stage primaryStage;
    private static Object dataHolder; // For passing data between scenes

    /**
     * Set the primary stage (called once from main app)
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Get the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Switch to a new scene
     * 
     * @param fxmlFile FXML file name (e.g., "login.fxml")
     */
    public static void switchScene(String fxmlFile) {
        switchScene(fxmlFile, 1200, 800);
    }

    /**
     * Switch to a new scene with custom dimensions
     * 
     * @param fxmlFile FXML file name
     * @param width    Window width
     * @param height   Window height
     */
    public static void switchScene(String fxmlFile, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            // Apply light theme only
            scene.getStylesheets().clear();
            scene.getStylesheets().add(SceneManager.class.getResource("/css/light-theme.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlFile);
            e.printStackTrace();
        }
    }

    /**
     * Switch scene and return the controller
     * Useful when you need to pass data to the new scene's controller
     */
    public static <T> T switchSceneAndGetController(String fxmlFile, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            // Apply current theme (light or dark)
            ThemeManager.applyTheme(scene);

            primaryStage.setScene(scene);
            primaryStage.show();

            return loader.getController();

        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlFile);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Store data to pass between scenes
     */
    public static void setData(Object data) {
        dataHolder = data;
    }

    /**
     * Retrieve data passed from previous scene
     */
    public static Object getData() {
        Object data = dataHolder;
        dataHolder = null; // Clear after retrieval
        return data;
    }
}
