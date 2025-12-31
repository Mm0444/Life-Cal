package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.service.ExerciseService;
import javafx.fxml.FXML;

/**
 * ExerciseCategoryController - Shows exercise categories
 */
public class ExerciseCategoryController {
    private ExerciseService exerciseService;

    public ExerciseCategoryController() {
        this.exerciseService = new ExerciseService();
    }

    @FXML
    private void handleCategoryClick(javafx.scene.input.MouseEvent event) {
        javafx.scene.layout.VBox vbox = (javafx.scene.layout.VBox) event.getSource();
        // Get the label from the VBox children
        javafx.scene.control.Label label = (javafx.scene.control.Label) vbox.getChildren().stream()
                .filter(node -> node instanceof javafx.scene.control.Label)
                .findFirst()
                .orElse(null);

        if (label != null) {
            String thaiCategory = label.getText();
            String englishCategory = mapToEnglish(thaiCategory);

            // Pass category to next screen
            SceneManager.setData(englishCategory);
            SceneManager.switchScene("exercise-list.fxml", 1200, 800);
        }
    }

    private String mapToEnglish(String thaiCategory) {
        switch (thaiCategory) {
            case "การเดิน":
                return "Walking";
            case "การวิ่ง":
                return "Running";
            case "การบริหารร่างกาย":
                return "Conditioning Exercise";
            case "กีฬา":
                return "Sports";
            case "การเต้น":
                return "Dancing";
            default:
                return "Walking";
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("dashboard.fxml", 1200, 800);
    }
}
