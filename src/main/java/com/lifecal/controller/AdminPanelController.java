package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.User;
import com.lifecal.service.AdminService;
import com.lifecal.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;

import java.util.List;
import java.util.Optional;

/**
 * AdminPanelController - Manages admin panel for user management
 */
public class AdminPanelController {
    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalAdminsLabel;

    @FXML
    private Label totalRegularUsersLabel;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, Double> currentWeightColumn;

    @FXML
    private TableColumn<User, Double> targetWeightColumn;

    @FXML
    private TableColumn<User, String> createdAtColumn;

    @FXML
    private TableColumn<User, Void> actionsColumn;

    // Food Table
    @FXML
    private TableView<com.lifecal.model.Food> foodsTable;

    // Exercise Table
    @FXML
    private TableView<com.lifecal.model.Exercise> exercisesTable;

    private AdminService adminService;
    private ObservableList<User> usersList;
    private ObservableList<com.lifecal.model.Food> foodsList;
    private ObservableList<com.lifecal.model.Exercise> exercisesList;

    @FXML
    private void initialize() {
        // Check authorization
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser == null || !currentUser.isAdmin()) {
            AlertUtil.showError("คุณไม่มีสิทธิ์เข้าถึงหน้านี้");
            SceneManager.switchScene("dashboard.fxml", 1200, 800);
            return;
        }

        this.adminService = new AdminService();

        // Setup table with delete button column
        setupDeleteButtonColumn();

        // Load data
        loadUsers();
        loadFoods();
        loadExercises();
    }

    /**
     * Setup the delete button column in table
     */
    private void setupDeleteButtonColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("ลบ");

            {
                deleteButton.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; " +
                        "-fx-font-size: 12px; -fx-padding: 5 15; -fx-cursor: hand;");
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    /**
     * Load all users from database
     */
    private void loadUsers() {
        List<User> users = adminService.getAllUsers();
        usersList = FXCollections.observableArrayList(users);
        usersTable.setItems(usersList);

        // Update summary stats
        updateStatistics(users);
    }

    private void loadFoods() {
        List<com.lifecal.model.Food> foods = adminService.getAllFoods();
        foodsList = FXCollections.observableArrayList(foods);
        foodsTable.setItems(foodsList);
    }

    private void loadExercises() {
        List<com.lifecal.model.Exercise> exercises = adminService.getAllExercises();
        exercisesList = FXCollections.observableArrayList(exercises);
        exercisesTable.setItems(exercisesList);
    }

    /**
     * Update summary statistics
     */
    private void updateStatistics(List<User> users) {
        int totalUsers = users.size();
        long adminCount = users.stream().filter(User::isAdmin).count();
        long regularUserCount = users.stream().filter(u -> !u.isAdmin()).count();

        totalUsersLabel.setText(String.valueOf(totalUsers));
        totalAdminsLabel.setText(String.valueOf(adminCount));
        totalRegularUsersLabel.setText(String.valueOf(regularUserCount));
    }

    private void handleDeleteUser(User user) {
        User currentUser = Session.getInstance().getCurrentUser();

        // Prevent deleting yourself
        if (user.getId() == currentUser.getId()) {
            AlertUtil.showError("คุณไม่สามารถลบบัญชีของตัวเองได้");
            return;
        }

        // Confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("ยืนยันการลบ");
        confirmDialog.setHeaderText("คุณแน่ใจหรือไม่ที่จะลบผู้ใช้นี้?");
        confirmDialog.setContentText("ผู้ใช้: " + user.getUsername() + "\n\n" +
                "⚠️ การดำเนินการนี้จะลบข้อมูลทั้งหมดของผู้ใช้:\n" +
                "- บันทึกอาหารทั้งหมด\n" +
                "- บันทึกการออกกำลังกายทั้งหมด\n" +
                "- บันทึกน้ำหนักทั้งหมด\n\n" +
                "และไม่สามารถกู้คืนได้!");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = adminService.deleteUser(user.getId());
            if (success) {
                AlertUtil.showSuccess("ลบผู้ใช้ " + user.getUsername() + " เรียบร้อยแล้ว");
                loadUsers(); // Reload table
            } else {
                AlertUtil.showError("ไม่สามารถลบผู้ใช้ได้ กรุณาลองอีกครั้ง");
            }
        }
    }

    @FXML
    private void handleAddFood() {
        Dialog<com.lifecal.model.Food> dialog = new Dialog<>();
        dialog.setTitle("เพิ่มอาหารใหม่");
        dialog.setHeaderText("กรอกข้อมูลอาหาร");

        ButtonType loginButtonType = new ButtonType("บันทึก", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("ชื่ออาหาร");

        ComboBox<String> category = new ComboBox<>();
        category.getItems().addAll("Protein", "Fat", "Carbohydrates", "Vegetables", "One-dish meals", "Beverages");
        category.setPromptText("เลือกหมวดหมู่");

        TextField calories = new TextField();
        calories.setPromptText("แคลอรี่");
        TextField protein = new TextField();
        protein.setPromptText("โปรตีน");
        TextField carbs = new TextField();
        carbs.setPromptText("คาร์โบไฮเดรต");
        TextField fat = new TextField();
        fat.setPromptText("ไขมัน");

        grid.add(new Label("ชื่ออาหาร:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("หมวดหมู่:"), 0, 1);
        grid.add(category, 1, 1);
        grid.add(new Label("แคลอรี่:"), 0, 2);
        grid.add(calories, 1, 2);
        grid.add(new Label("โปรตีน:"), 0, 3);
        grid.add(protein, 1, 3);
        grid.add(new Label("คาร์โบไฮเดรต:"), 0, 4);
        grid.add(carbs, 1, 4);
        grid.add(new Label("ไขมัน:"), 0, 5);
        grid.add(fat, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                try {
                    com.lifecal.model.Food food = new com.lifecal.model.Food();
                    food.setName(name.getText());
                    food.setCategory(category.getValue());
                    food.setKcalPerServing(Double.parseDouble(calories.getText()));
                    food.setProtein(Double.parseDouble(protein.getText()));
                    food.setCarbs(Double.parseDouble(carbs.getText()));
                    food.setFat(Double.parseDouble(fat.getText()));
                    return food;
                } catch (NumberFormatException e) {
                    AlertUtil.showError("กรุณากรอกตัวเลขให้ถูกต้อง");
                    return null;
                }
            }
            return null;
        });

        Optional<com.lifecal.model.Food> result = dialog.showAndWait();

        result.ifPresent(food -> {
            if (food.getName() == null || food.getName().isEmpty() || food.getCategory() == null) {
                AlertUtil.showError("กรุณากรอกชื่อและเลือกหมวดหมู่");
                return;
            }
            if (adminService.addFood(food)) {
                AlertUtil.showSuccess("เพิ่มอาหารเรียบร้อยแล้ว");
                loadFoods();
            } else {
                AlertUtil.showError("ไม่สามารถเพิ่มอาหารได้");
            }
        });
    }

    @FXML
    private void handleAddExercise() {
        Dialog<com.lifecal.model.Exercise> dialog = new Dialog<>();
        dialog.setTitle("เพิ่มการออกกำลังกายใหม่");
        dialog.setHeaderText("กรอกข้อมูลการออกกำลังกาย");

        ButtonType loginButtonType = new ButtonType("บันทึก", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("ชื่อการออกกำลังกาย");

        ComboBox<String> category = new ComboBox<>();
        category.getItems().addAll("Walking", "Running", "Conditioning Exercise", "Sports", "Dancing");
        category.setPromptText("เลือกหมวดหมู่");

        TextField calories = new TextField();
        calories.setPromptText("แคลอรี่ต่อนาที");

        grid.add(new Label("ชื่อการออกกำลังกาย:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("หมวดหมู่:"), 0, 1);
        grid.add(category, 1, 1);
        grid.add(new Label("แคลอรี่/นาที:"), 0, 2);
        grid.add(calories, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                try {
                    com.lifecal.model.Exercise exercise = new com.lifecal.model.Exercise();
                    exercise.setName(name.getText());
                    exercise.setCategory(category.getValue());
                    exercise.setKcalPerMinute(Double.parseDouble(calories.getText()));
                    return exercise;
                } catch (NumberFormatException e) {
                    AlertUtil.showError("กรุณากรอกตัวเลขให้ถูกต้อง");
                    return null;
                }
            }
            return null;
        });

        Optional<com.lifecal.model.Exercise> result = dialog.showAndWait();

        result.ifPresent(exercise -> {
            if (exercise.getName() == null || exercise.getName().isEmpty() || exercise.getCategory() == null) {
                AlertUtil.showError("กรุณากรอกชื่อและเลือกหมวดหมู่");
                return;
            }
            if (adminService.addExercise(exercise)) {
                AlertUtil.showSuccess("เพิ่มการออกกำลังกายเรียบร้อยแล้ว");
                loadExercises();
            } else {
                AlertUtil.showError("ไม่สามารถเพิ่มการออกกำลังกายได้");
            }
        });
    }

    @FXML
    private void handleRefresh() {
        loadUsers();
        loadFoods();
        loadExercises();
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("dashboard.fxml", 1200, 800);
    }
}
