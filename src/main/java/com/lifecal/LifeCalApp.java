package com.lifecal;

import com.lifecal.common.DatabaseManager;
import com.lifecal.common.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * LifeCalApp - Main application entry point
 * 
 * To run: mvn javafx:run
 * To build JAR: mvn clean package
 */
public class LifeCalApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            DatabaseManager.getInstance();

            // Set up scene manager
            SceneManager.setPrimaryStage(primaryStage);

            // Configure primary stage
            primaryStage.setTitle("LIFE CAL - Health Tracking & Calorie Management");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            // Load initial login scene
            SceneManager.switchScene("login.fxml", 1200, 800);

            // Show stage
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Failed to start application!");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Clean up resources
        DatabaseManager.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
