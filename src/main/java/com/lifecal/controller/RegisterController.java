package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.model.User;
import com.lifecal.service.AuthService;
import com.lifecal.util.AlertUtil;
import com.lifecal.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * RegisterController - Handles user registration
 */
public class RegisterController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

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

    private AuthService authService;

    public RegisterController() {
        this.authService = new AuthService();
    }

    @FXML
    private void initialize() {
        // Populate ComboBoxes
        activityLevelCombo.getItems().addAll("เร็วมาก", "เร็ว", "ปกติ", "ช้า");
        activityLevelCombo.setValue("ปกติ");

        exerciseFrequencyCombo.getItems().addAll("1-3 ครั้ง/สัปดาห์", "4-5 ครั้ง/สัปดาห์", "6-7 ครั้ง/สัปดาห์");
        exerciseFrequencyCombo.setValue("1-3 ครั้ง/สัปดาห์");
    }

    @FXML
    private void handleRegister() {
        // Get form values
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String weightStr = weightField.getText().trim();
        String heightStr = heightField.getText().trim();
        String targetWeightStr = targetWeightField.getText().trim();
        String activityLevel = activityLevelCombo.getValue();
        String exerciseFrequency = exerciseFrequencyCombo.getValue();

        // Validation
        if (!ValidationUtil.isValidUsername(username)) {
            AlertUtil.showError(
                    "ชื่อผู้ใช้ต้องมีความยาว 3-20 ตัวอักษร และประกอบด้วยตัวอักษร ตัวเลข หรือขีดล่างเท่านั้น");
            return;
        }

        // Validate email if provided
        if (!ValidationUtil.isValidEmail(email)) {
            AlertUtil.showError("รูปแบบอีเมลไม่ถูกต้อง กรุณาใช้รูปแบบ example@mail.com");
            return;
        }

        // Check if email already exists (only if email is provided)
        if (!email.isEmpty() && authService.isEmailTaken(email)) {
            AlertUtil.showError("อีเมลนี้ถูกใช้งานแล้ว กรุณาใช้อีเมลอื่น");
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            AlertUtil.showError("รหัสผ่านต้องมีความยาวอย่างน้อย 4 ตัวอักษร");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertUtil.showError("รหัสผ่านไม่ตรงกัน");
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

        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email.isEmpty() ? null : email); // Set null if empty
        user.setCurrentWeight(weight);
        user.setHeight(height);
        user.setTargetWeight(targetWeight);
        user.setActivityLevel(activityLevel);
        user.setExerciseFrequency(exerciseFrequency);
        user.setProfileImage(null); // Default profile image

        // Register
        boolean success = authService.register(user, password);

        if (success) {
            AlertUtil.showSuccess("ลงทะเบียนสำเร็จ! กรุณาเข้าสู่ระบบ");
            handleBack();
        } else {
            AlertUtil.showError("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว กรุณาเลือกชื่ออื่น");
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("login.fxml", 1200, 800);
    }
}
