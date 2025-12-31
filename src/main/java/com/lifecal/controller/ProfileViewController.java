package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.User;
import com.lifecal.service.UserService;
import com.lifecal.util.AlertUtil;
import com.lifecal.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * ProfileViewController - Displays user profile with option to edit
 */
public class ProfileViewController {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label weightLabel;

    @FXML
    private Label heightLabel;

    @FXML
    private Label targetWeightLabel;

    @FXML
    private Label activityLevelLabel;

    @FXML
    private Label exerciseFrequencyLabel;

    private User currentUser;

    @FXML
    private void initialize() {
        loadProfile();
    }

    private void loadProfile() {
        currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());

            // Display email or '-' if not set
            String email = currentUser.getEmail();
            emailLabel.setText((email != null && !email.isEmpty()) ? email : "-");

            weightLabel.setText(String.format("%.1f กก.", currentUser.getCurrentWeight()));
            heightLabel.setText(String.format("%.1f ซม.", currentUser.getHeight()));
            targetWeightLabel.setText(String.format("%.1f กก.", currentUser.getTargetWeight()));
            activityLevelLabel.setText(currentUser.getActivityLevel());
            exerciseFrequencyLabel.setText(currentUser.getExerciseFrequency());
        }
    }

    @FXML
    private void handleEdit() {
        SceneManager.switchScene("profile-edit.fxml", 1200, 800);
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("dashboard.fxml", 1200, 800);
    }
}
