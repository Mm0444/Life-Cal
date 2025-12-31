package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.ExerciseLog;
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

public class DailyExerciseLogController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<ExerciseLog> exerciseLogTable;
    @FXML
    private TableColumn<ExerciseLog, String> exerciseNameColumn;
    @FXML
    private TableColumn<ExerciseLog, Integer> durationColumn;
    @FXML
    private TableColumn<ExerciseLog, Double> caloriesColumn;
    @FXML
    private TableColumn<ExerciseLog, Void> actionColumn;
    @FXML
    private Label totalCaloriesLabel;

    private LogService logService;
    private ObservableList<ExerciseLog> exerciseLogs;
    private int currentUserId;

    @FXML
    private void initialize() {
        logService = new LogService();
        currentUserId = Session.getInstance().getCurrentUser().getId();
        exerciseLogs = FXCollections.observableArrayList();

        datePicker.setValue(LocalDate.now());

        setupTableColumns();
        loadExerciseLogs();

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadExerciseLogs();
            }
        });
    }

    private void setupTableColumns() {
        exerciseNameColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExerciseName()));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("caloriesBurned"));

        actionColumn.setCellFactory(param -> new TableCell<ExerciseLog, Void>() {
            private final Button deleteBtn = new Button("ลบ");

            {
                deleteBtn.getStyleClass().add("button-danger");
                deleteBtn.setOnAction(event -> {
                    ExerciseLog log = getTableView().getItems().get(getIndex());
                    handleDeleteLog(log);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        exerciseLogTable.setItems(exerciseLogs);
    }

    private void loadExerciseLogs() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null)
            return;

        List<ExerciseLog> logs = logService.getExerciseLogsByDate(currentUserId, selectedDate);
        exerciseLogs.setAll(logs);
        updateTotalCalories();
    }

    private void updateTotalCalories() {
        double total = exerciseLogs.stream().mapToDouble(ExerciseLog::getCaloriesBurned).sum();
        totalCaloriesLabel.setText(String.format("%.0f กิโลแคลอรี่", total));
    }

    private void handleDeleteLog(ExerciseLog log) {
        boolean confirm = AlertUtil.showConfirmation("คุณแน่ใจหรือไม่ว่าต้องการลบรายการนี้?");
        if (confirm) {
            boolean success = logService.deleteExerciseLog(log.getId());
            if (success) {
                AlertUtil.showSuccess("ลบรายการสำเร็จ!");
                loadExerciseLogs();
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
