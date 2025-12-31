package com.lifecal.controller;

import com.lifecal.util.AlertUtil;
import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.Exercise;
import com.lifecal.model.ExerciseLog;
import com.lifecal.service.ExerciseService;
import com.lifecal.service.LogService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * ExerciseListController - Shows exercises in selected category with logging
 * capability
 */
public class ExerciseListController {
    @FXML
    private Label categoryLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField durationField;
    @FXML
    private TableView<Exercise> exerciseTable;
    @FXML
    private TableColumn<Exercise, String> nameColumn;
    @FXML
    private TableColumn<Exercise, Double> caloriesPerMinColumn;
    @FXML
    private TableColumn<Exercise, Void> actionColumn;

    private ExerciseService exerciseService;
    private LogService logService;
    private String category;

    public ExerciseListController() {
        this.exerciseService = new ExerciseService();
        this.logService = new LogService();
    }

    @FXML
    private void initialize() {
        // Get category from previous screen
        category = (String) SceneManager.getData();
        if (category == null) {
            category = "Walking"; // Default
        }

        categoryLabel.setText(mapToThai(category));

        // Initialize date picker
        datePicker.setValue(LocalDate.now());

        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        caloriesPerMinColumn.setCellValueFactory(new PropertyValueFactory<>("kcalPerMinute"));

        // Set up action column with buttons
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button logButton = new Button("บันทึก");

            {
                logButton.setOnAction(event -> {
                    Exercise exercise = getTableView().getItems().get(getIndex());
                    handleAddExerciseLog(exercise);
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

        // Load exercises for this category
        loadExercises();
    }

    private void loadExercises() {
        List<Exercise> exercises = exerciseService.getExercisesByCategory(category);
        exerciseTable.setItems(FXCollections.observableArrayList(exercises));
    }

    private void handleAddExerciseLog(Exercise exercise) {
        // Validation
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            AlertUtil.showWarning("กรุณาเลือกวันที่");
            return;
        }

        String durationText = durationField.getText().trim();
        if (durationText.isEmpty()) {
            AlertUtil.showWarning("กรุณากรอกระยะเวลา");
            return;
        }

        double duration;
        try {
            duration = Double.parseDouble(durationText);
            if (duration <= 0) {
                AlertUtil.showWarning("ระยะเวลาต้องมากกว่า 0");
                return;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showWarning("กรุณากรอกระยะเวลาที่ถูกต้อง");
            return;
        }

        // Get current user
        int currentUserId = Session.getInstance().getCurrentUser().getId();

        // Calculate total calories
        double totalCalories = exercise.getKcalPerMinute() * duration;

        // Create exercise log
        ExerciseLog log = new ExerciseLog();
        log.setUserId(currentUserId);
        log.setExerciseId(exercise.getId());
        log.setDate(selectedDate);
        log.setDurationMinutes((int) duration);
        log.setCaloriesBurned(totalCalories);

        // Save log
        boolean success = logService.addExerciseLog(log);
        if (success) {
            AlertUtil.showSuccess("บันทึกสำเร็จ!");
            // Clear duration field for next entry
            durationField.clear();
        } else {
            AlertUtil.showError("บันทึกไม่สำเร็จ");
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("exercise-category.fxml", 1200, 800);
    }

    private String mapToThai(String englishCategory) {
        switch (englishCategory) {
            case "Walking":
                return "การเดิน";
            case "Running":
                return "การวิ่ง";
            case "Conditioning Exercise":
                return "การบริหารร่างกาย";
            case "Sports":
                return "กีฬา";
            case "Dancing":
                return "การเต้น";
            default:
                return englishCategory;
        }
    }
}
