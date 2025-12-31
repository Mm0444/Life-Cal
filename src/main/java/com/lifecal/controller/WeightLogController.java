package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.WeightLog;
import com.lifecal.service.LogService;
import com.lifecal.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeightLogController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField weightField;
    @FXML
    private TableView<WeightLog> weightLogTable;
    @FXML
    private TableColumn<WeightLog, String> dateColumn;
    @FXML
    private TableColumn<WeightLog, Double> weightColumn;
    @FXML
    private TableColumn<WeightLog, Void> actionColumn;
    @FXML
    private Label currentWeightLabel;
    @FXML
    private Label previousWeightLabel;
    @FXML
    private Label changeLabel;

    private LogService logService;
    private ObservableList<WeightLog> weightLogs;
    private int currentUserId;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        logService = new LogService();
        currentUserId = Session.getInstance().getCurrentUser().getId();
        weightLogs = FXCollections.observableArrayList();

        datePicker.setValue(LocalDate.now());
        setupTableColumns();
        loadWeightLogs();
    }

    private void setupTableColumns() {
        dateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getDate().format(dateFormatter)));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        actionColumn.setCellFactory(param -> new TableCell<WeightLog, Void>() {
            private final Button deleteBtn = new Button("ลบ");

            {
                deleteBtn.getStyleClass().add("button-danger");
                deleteBtn.setOnAction(event -> {
                    WeightLog log = getTableView().getItems().get(getIndex());
                    handleDeleteLog(log);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        weightLogTable.setItems(weightLogs);
    }

    private void loadWeightLogs() {
        List<WeightLog> logs = logService.getWeightHistory(currentUserId);
        weightLogs.setAll(logs);
        updateStatistics();
    }

    private void updateStatistics() {
        if (weightLogs.isEmpty()) {
            currentWeightLabel.setText("N/A");
            previousWeightLabel.setText("N/A");
            changeLabel.setText("N/A");
            changeLabel.setStyle("");
            return;
        }

        WeightLog latest = weightLogs.get(weightLogs.size() - 1);
        currentWeightLabel.setText(String.format("%.1f กก.", latest.getWeight()));

        if (weightLogs.size() > 1) {
            WeightLog previous = weightLogs.get(weightLogs.size() - 2);
            previousWeightLabel.setText(String.format("%.1f กก.", previous.getWeight()));

            double change = latest.getWeight() - previous.getWeight();
            String changeText = String.format("%.1f กก.", Math.abs(change));

            if (change > 0) {
                changeLabel.setText("+" + changeText);
                changeLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
            } else if (change < 0) {
                changeLabel.setText("-" + changeText);
                changeLabel.setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
            } else {
                changeLabel.setText(changeText);
                changeLabel.setStyle("-fx-text-fill: #6B7280;");
            }
        } else {
            previousWeightLabel.setText("N/A");
            changeLabel.setText("N/A");
            changeLabel.setStyle("");
        }
    }

    @FXML
    private void handleAddEntry() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            AlertUtil.showWarning("กรุณาเลือกวันที่");
            return;
        }

        String weightText = weightField.getText().trim();
        if (weightText.isEmpty()) {
            AlertUtil.showWarning("กรุณาระบุน้ำหนักของคุณ");
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightText);
            if (weight <= 0 || weight > 500) {
                AlertUtil.showWarning("กรุณาระบุน้ำหนักที่ถูกต้อง (0-500 กก.)");
                return;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showWarning("กรุณาระบุตัวเลขที่ถูกต้องสำหรับน้ำหนัก");
            return;
        }

        WeightLog existingLog = logService.getWeightForDate(currentUserId, selectedDate);
        if (existingLog != null) {
            AlertUtil.showWarning(
                    "มีรายการน้ำหนักสำหรับวันนี้แล้ว กรุณาลบรายการเดิมก่อนหรือเลือกวันที่อื่น");
            return;
        }

        WeightLog log = new WeightLog();
        log.setUserId(currentUserId);
        log.setDate(selectedDate);
        log.setWeight(weight);

        boolean success = logService.addWeightLog(log);
        if (success) {
            // Update user's current weight in Session and Database
            com.lifecal.model.User currentUser = Session.getInstance().getCurrentUser();
            currentUser.setCurrentWeight(weight);

            com.lifecal.service.UserService userService = new com.lifecal.service.UserService();
            userService.updateProfile(currentUser);

            AlertUtil.showSuccess("เพิ่มรายการน้ำหนักสำเร็จ!");
            weightField.clear();
            datePicker.setValue(LocalDate.now());
            loadWeightLogs();
        } else {
            AlertUtil.showError("เพิ่มรายการน้ำหนักไม่สำเร็จ");
        }
    }

    private void handleDeleteLog(WeightLog log) {
        boolean confirm = AlertUtil.showConfirmation(
                String.format("คุณแน่ใจหรือไม่ว่าต้องการลบรายการน้ำหนักสำหรับวันที่ %s?",
                        log.getDate().format(dateFormatter)));

        if (confirm) {
            boolean success = logService.deleteWeightLog(log.getId());
            if (success) {
                AlertUtil.showSuccess("ลบรายการสำเร็จ!");
                loadWeightLogs();

                // Update user's current weight to the new latest weight (or keep existing if no
                // logs)
                if (!weightLogs.isEmpty()) {
                    WeightLog latest = weightLogs.get(weightLogs.size() - 1);
                    double newCurrentWeight = latest.getWeight();

                    com.lifecal.model.User currentUser = Session.getInstance().getCurrentUser();
                    currentUser.setCurrentWeight(newCurrentWeight);

                    com.lifecal.service.UserService userService = new com.lifecal.service.UserService();
                    userService.updateProfile(currentUser);
                }

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
