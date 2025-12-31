package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.User;
import com.lifecal.service.AuthService;
import com.lifecal.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * LoginController - Handles login screen logic
 */
public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("กรุณาระบุชื่อผู้ใช้และรหัสผ่าน");
            return;
        }

        User user = authService.login(username, password);

        if (user != null) {
            // Login successful
            Session.getInstance().setCurrentUser(user);
            SceneManager.switchScene("dashboard.fxml", 1200, 800);
        } else {
            AlertUtil.showError("ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
        }
    }

    @FXML
    private void handleRegister() {
        SceneManager.switchScene("register.fxml", 1200, 800);
    }
}
