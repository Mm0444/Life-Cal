package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.User;
import com.lifecal.repository.UserRepository;
import com.lifecal.service.UserService;
import com.lifecal.util.AlertUtil;
import com.lifecal.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * ProfileEditController - Edit user profile
 */
public class ProfileEditController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField targetWeightField;

    @FXML
    private ComboBox<String> activityLevelCombo;

    @FXML
    private ComboBox<String> exerciseFrequencyCombo;

    private UserService userService;
    private UserRepository userRepository;
    private User currentUser;

    public ProfileEditController() {
        this.userService = new UserService();
        this.userRepository = new UserRepository();
    }

    @FXML
    private void initialize() {
        // Populate ComboBoxes
        activityLevelCombo.getItems().addAll("เร็วมาก", "เร็ว", "ปกติ", "ช้า");
        exerciseFrequencyCombo.getItems().addAll("1-3 ครั้ง/สัปดาห์", "4-5 ครั้ง/สัปดาห์", "6-7 ครั้ง/สัปดาห์");

        // Load current profile data
        loadProfile();
    }

    private void loadProfile() {
        currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            weightField.setText(String.format("%.1f", currentUser.getCurrentWeight()));
            heightField.setText(String.format("%.1f", currentUser.getHeight()));
            targetWeightField.setText(String.format("%.1f", currentUser.getTargetWeight()));
            activityLevelCombo.setValue(currentUser.getActivityLevel());
            exerciseFrequencyCombo.setValue(currentUser.getExerciseFrequency());
        }
    }

    @FXML
    private void handleSave() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String weightStr = weightField.getText().trim();
        String heightStr = heightField.getText().trim();
        String targetWeightStr = targetWeightField.getText().trim();

        // Validation
        if (!ValidationUtil.isValidUsername(username)) {
            AlertUtil.showError("รูปแบบชื่อผู้ใช้ไม่ถูกต้อง");
            return;
        }

        // Validate email if provided
        if (!ValidationUtil.isValidEmail(email)) {
            AlertUtil.showError("รูปแบบอีเมลไม่ถูกต้อง กรุณาใช้รูปแบบ example@mail.com");
            return;
        }

        // Check if email is taken by another user
        if (!email.isEmpty() && userRepository.existsByEmailExcludingUser(email, currentUser.getId())) {
            AlertUtil.showError("อีเมลนี้ถูกใช้งานแล้ว กรุณาใช้อีเมลอื่น");
            return;
        }

        if (!ValidationUtil.isPositiveNumber(weightStr) ||
                !ValidationUtil.isPositiveNumber(heightStr) ||
                !ValidationUtil.isPositiveNumber(targetWeightStr)) {
            AlertUtil.showError("กรุณาระบุตัวเลขที่ถูกต้องสำหรับน้ำหนักและส่วนสูง");
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr);
        double targetWeight = Double.parseDouble(targetWeightStr);

        if (!ValidationUtil.isValidWeight(weight) || !ValidationUtil.isValidWeight(targetWeight)) {
            AlertUtil.showError("น้ำหนักต้องอยู่ระหว่าง 20 ถึง 500 กก.");
            return;
        }

        if (!ValidationUtil.isValidHeight(height)) {
            AlertUtil.showError("ส่วนสูงต้องอยู่ระหว่าง 50 ถึง 300 ซม.");
            return;
        }

        // Check if target weight changed significantly
        if (Math.abs(currentUser.getTargetWeight() - targetWeight) > 0.1) {
            // Target changed, reset goal start weight to current weight
            currentUser.setGoalStartWeight(weight);
        }

        currentUser.setUsername(username);
        currentUser.setEmail(email.isEmpty() ? null : email);
        currentUser.setCurrentWeight(weight);
        currentUser.setHeight(height);
        currentUser.setTargetWeight(targetWeight);
        currentUser.setActivityLevel(activityLevelCombo.getValue());
        currentUser.setExerciseFrequency(exerciseFrequencyCombo.getValue());

        boolean success = userService.updateProfile(currentUser);

        if (success) {
            Session.getInstance().setCurrentUser(currentUser);
            AlertUtil.showSuccess("อัปเดตโปรไฟล์สำเร็จ!");
            SceneManager.switchScene("dashboard.fxml", 1200, 800);
        } else {
            AlertUtil.showError("อัปเดตโปรไฟล์ไม่สำเร็จ");
        }
    }

    @FXML
    private void handleCancel() {
        SceneManager.switchScene("profile-view.fxml", 1200, 800);
    }
}
