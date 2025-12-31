package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.FoodLog;
import com.lifecal.service.LogService;
import com.lifecal.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class DailyFoodLogController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<FoodLog> foodLogTable;
    @FXML
    private TableColumn<FoodLog, String> foodNameColumn;
    @FXML
    private TableColumn<FoodLog, String> mealTimeColumn;
    @FXML
    private TableColumn<FoodLog, Double> quantityColumn;
    @FXML
    private TableColumn<FoodLog, Double> caloriesColumn;
    @FXML
    private TableColumn<FoodLog, Void> actionColumn;
    @FXML
    private Label totalCaloriesLabel;

    private LogService logService;
    private ObservableList<FoodLog> foodLogs;
    private int currentUserId;

    @FXML
    private void initialize() {
        logService = new LogService();
        currentUserId = Session.getInstance().getCurrentUser().getId();
        foodLogs = FXCollections.observableArrayList();

        datePicker.setValue(LocalDate.now());

        setupTableColumns();
        loadFoodLogs();

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadFoodLogs();
            }
        });
    }

    private void setupTableColumns() {
        foodNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFoodName()));
        mealTimeColumn.setCellValueFactory(new PropertyValueFactory<>("mealTime"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCalories"));

        actionColumn.setCellFactory(param -> new TableCell<FoodLog, Void>() {
            private final Button deleteBtn = new Button("ลบ");

            {
                deleteBtn.getStyleClass().add("button-danger");
                deleteBtn.setOnAction(event -> {
                    FoodLog log = getTableView().getItems().get(getIndex());
                    handleDeleteLog(log);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        foodLogTable.setItems(foodLogs);
    }

    private void loadFoodLogs() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null)
            return;

        List<FoodLog> logs = logService.getFoodLogsByDate(currentUserId, selectedDate);
        foodLogs.setAll(logs);
        updateTotalCalories();
    }

    private void updateTotalCalories() {
        double total = foodLogs.stream().mapToDouble(FoodLog::getTotalCalories).sum();
        totalCaloriesLabel.setText(String.format("%.0f กิโลแคลอรี่", total));
    }

    private void handleDeleteLog(FoodLog log) {
        boolean confirm = AlertUtil.showConfirmation("คุณแน่ใจหรือไม่ว่าต้องการลบรายการนี้?");
        if (confirm) {
            boolean success = logService.deleteFoodLog(log.getId());
            if (success) {
                AlertUtil.showSuccess("ลบรายการสำเร็จ!");
                loadFoodLogs();
            } else {
                AlertUtil.showError("ลบรายการไม่สำเร็จ");
            }
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("dashboard.fxml", 1200, 800);
    }
}
