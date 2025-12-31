package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.User;
import com.lifecal.service.HealthCalculator;
import com.lifecal.service.LogService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

/**
 * DashboardController - Main dashboard after login
 */
public class DashboardController {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label bmiLabel;

    @FXML
    private Label bmiCategoryLabel;

    @FXML
    private Label bmrLabel;

    @FXML
    private Label currentWeightLabel;

    @FXML
    private Label targetWeightLabel;

    @FXML
    private Label progressPercentLabel;

    @FXML
    private ProgressBar weightProgressBar;

    @FXML
    private javafx.scene.layout.VBox goalReachedBox;

    @FXML
    private Label maintenanceLabel;

    @FXML
    private Label progressMessageLabel;

    @FXML
    private Button adminMenuButton;

    @FXML
    private void initialize() {
        loadUserData();

        // Show/hide admin button based on role
        User user = Session.getInstance().getCurrentUser();
        if (user != null && user.isAdmin()) {
            adminMenuButton.setVisible(true);
            adminMenuButton.setManaged(true);
        }
    }

    private void loadUserData() {
        User user = Session.getInstance().getCurrentUser();
        if (user == null) {
            // No user logged in, redirect to login
            SceneManager.switchScene("login.fxml");
            return;
        }

        // Display username
        usernameLabel.setText("ยินดีต้อนรับ, " + user.getUsername() + "!");

        // Calculate and display BMI
        double bmi = HealthCalculator.calculateBMI(user.getCurrentWeight(), user.getHeight());
        bmiLabel.setText(String.format("%.1f", bmi));
        bmiCategoryLabel.setText(HealthCalculator.getBMICategory(bmi));

        // Calculate and display BMR
        double bmr = HealthCalculator.calculateBMR(user.getCurrentWeight(), user.getHeight());
        bmrLabel.setText(String.format("%.0f กิโลแคลอรี่/วัน", bmr));

        // Display weights
        currentWeightLabel.setText(String.format("%.1f กก.", user.getCurrentWeight()));
        targetWeightLabel.setText(String.format("%.1f กก.", user.getTargetWeight()));

        // Calculate weight progress
        double startWeight = user.getGoalStartWeight();

        // Fallback: If goal start weight is 0 (legacy data), try to get from logs
        if (startWeight == 0) {
            LogService logService = new LogService();
            java.util.List<com.lifecal.model.WeightLog> weightHistory = logService.getWeightHistory(user.getId());

            if (!weightHistory.isEmpty()) {
                startWeight = weightHistory.get(0).getWeight();

                // Auto-fix: Update user's goal start weight for future
                user.setGoalStartWeight(startWeight);
                // We could save this to DB here, but let's keep it simple for now
            } else {
                startWeight = user.getCurrentWeight();
            }
        }

        double progress = HealthCalculator.calculateWeightProgress(
                startWeight,
                user.getCurrentWeight(),
                user.getTargetWeight());
        progressPercentLabel.setText(String.format("%.0f%%", progress));
        weightProgressBar.setProgress(progress / 100.0);

        // Check if goal reached
        if (progress >= 100.0) {
            goalReachedBox.setVisible(true);
            goalReachedBox.setManaged(true);
            progressMessageLabel.setVisible(false);
            progressMessageLabel.setManaged(false);

            // Calculate maintenance calories (TDEE)
            // Assuming "Normal" activity level for now as it's not in User model yet
            double maintenanceCalories = HealthCalculator.calculateTDEE(bmr, "Normal");

            if (progress > 100.0) {
                // Goal Surpassed
                double diff = Math.abs(user.getCurrentWeight() - user.getTargetWeight());
                ((Label) goalReachedBox.getChildren().get(0)).setText("🎉 เกินเป้าหมายแล้ว! 🎉");
                maintenanceLabel.setText(String.format(
                        "คุณทำได้เกินเป้าหมาย %.1f กก.!\nเพื่อรักษาน้ำหนักใหม่นี้ ควรบริโภค ~%.0f กิโลแคลอรี่/วัน",
                        diff, maintenanceCalories));
            } else {
                // Exact Goal Reached
                ((Label) goalReachedBox.getChildren().get(0)).setText("🎉 ถึงเป้าหมายแล้ว! 🎉");
                maintenanceLabel.setText(
                        String.format("เพื่อรักษาน้ำหนักนี้ ควรบริโภคประมาณ %.0f กิโลแคลอรี่/วัน",
                                maintenanceCalories));
            }
        } else {
            goalReachedBox.setVisible(false);
            goalReachedBox.setManaged(false);
            progressMessageLabel.setVisible(true);
            progressMessageLabel.setManaged(true);
            progressMessageLabel.setText("ติดตามความคืบหน้าของคุณต่อไป!");
        }
    }

    @FXML
    private void handleProfile() {
        SceneManager.switchScene("profile-view.fxml", 1200, 800);
    }

    @FXML
    private void handleFood() {
        SceneManager.switchScene("food-category.fxml", 1200, 800);
    }

    @FXML
    private void handleExercise() {
        SceneManager.switchScene("exercise-category.fxml", 1200, 800);
    }

    @FXML
    private void handleFoodLog() {
        SceneManager.switchScene("daily-food-log.fxml", 1200, 800);
    }

    @FXML
    private void handleExerciseLog() {
        SceneManager.switchScene("daily-exercise-log.fxml", 1200, 800);
    }

    @FXML
    private void handleWeightLog() {
        SceneManager.switchScene("weight-log.fxml", 1200, 800);
    }

    @FXML
    private void handleCalorieReport() {
        SceneManager.switchScene("calorie-report.fxml", 1200, 800);
    }

    @FXML
    private void handleWeightReport() {
        SceneManager.switchScene("weight-report.fxml", 1200, 800);
    }

    @FXML
    private void handleAdminPanel() {
        SceneManager.switchScene("admin-panel.fxml", 1200, 800);
    }

    @FXML
    private void handleLogout() {
        Session.getInstance().clear();
        SceneManager.switchScene("login.fxml");
    }
}
