package com.lifecal.controller;

import com.lifecal.util.AlertUtil;
import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.Food;
import com.lifecal.model.FoodLog;
import com.lifecal.service.FoodService;
import com.lifecal.service.LogService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * FoodListController - Shows foods in selected category with logging capability
 */
public class FoodListController {
    @FXML
    private Label categoryLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> mealTimeComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private TableView<Food> foodTable;
    @FXML
    private TableColumn<Food, String> nameColumn;
    @FXML
    private TableColumn<Food, Double> caloriesColumn;
    @FXML
    private TableColumn<Food, Double> proteinColumn;
    @FXML
    private TableColumn<Food, Double> carbsColumn;
    @FXML
    private TableColumn<Food, Double> fatColumn;
    @FXML
    private TableColumn<Food, Void> actionColumn;

    private FoodService foodService;
    private LogService logService;
    private String category;

    public FoodListController() {
        this.foodService = new FoodService();
        this.logService = new LogService();
    }

    @FXML
    private void initialize() {
        // Get category from previous screen
        category = (String) SceneManager.getData();
        if (category == null) {
            category = "Protein"; // Default
        }

        categoryLabel.setText(mapToThai(category));

        // Initialize date picker and meal time
        datePicker.setValue(LocalDate.now());
        mealTimeComboBox.getItems().addAll("อาหารเช้า", "อาหารกลางวัน", "อาหารเย็น", "ของว่าง");
        mealTimeComboBox.setValue("อาหารเช้า");

        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("kcalPerServing"));
        proteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        fatColumn.setCellValueFactory(new PropertyValueFactory<>("fat"));

        // Set up action column with buttons
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button logButton = new Button("บันทึก");

            {
                logButton.setOnAction(event -> {
                    Food food = getTableView().getItems().get(getIndex());
                    handleAddFoodLog(food);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(logButton);
                }
            }
        });

        // Load foods for this category
        loadFoods();
    }

    private void loadFoods() {
        List<Food> foods = foodService.getFoodsByCategory(category);
        foodTable.setItems(FXCollections.observableArrayList(foods));
    }

    private void handleAddFoodLog(Food food) {
        // Validation
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            AlertUtil.showWarning("กรุณาเลือกวันที่");
            return;
        }

        String mealTime = mealTimeComboBox.getValue();
        if (mealTime == null) {
            AlertUtil.showWarning("กรุณาเลือกมื้ออาหาร");
            return;
        }

        String quantityText = quantityField.getText().trim();
        if (quantityText.isEmpty()) {
            AlertUtil.showWarning("กรุณากรอกจำนวน");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
            if (quantity <= 0) {
                AlertUtil.showWarning("จำนวนต้องมากกว่า 0");
                return;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showWarning("กรุณากรอกจำนวนที่ถูกต้อง");
            return;
        }

        // Get current user
        int currentUserId = Session.getInstance().getCurrentUser().getId();

        // Calculate total calories
        double totalCalories = food.getKcalPerServing() * quantity;

        // Create food log
        FoodLog log = new FoodLog();
        log.setUserId(currentUserId);
        log.setFoodId(food.getId());
        log.setDate(selectedDate);
        log.setQuantity(quantity);
        log.setTotalCalories(totalCalories);
        log.setMealTime(mealTime);

        // Save log
        boolean success = logService.addFoodLog(log);
        if (success) {
            AlertUtil.showSuccess("บันทึกสำเร็จ!");
            // Clear quantity field for next entry
            quantityField.clear();
        } else {
            AlertUtil.showError("บันทึกไม่สำเร็จ");
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("food-category.fxml", 1200, 800);
    }

    private String mapToThai(String englishCategory) {
        switch (englishCategory) {
            case "Protein":
                return "โปรตีน";
            case "Fat":
                return "ไขมัน";
            case "Carbohydrates":
                return "คาร์โบไฮเดรต";
            case "Vegetables":
                return "ผัก";
            case "One-dish meals":
                return "อาหารจานเดียว";
            case "Beverages":
                return "เครื่องดื่ม";
            default:
                return englishCategory;
        }
    }
}
