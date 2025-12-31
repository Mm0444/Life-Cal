package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.service.FoodService;
import javafx.fxml.FXML;

/**
 * FoodCategoryController - Shows food categories
 */
public class FoodCategoryController {
    private FoodService foodService;

    public FoodCategoryController() {
        this.foodService = new FoodService();
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
            SceneManager.switchScene("food-list.fxml", 1200, 800);
        }
    }

    private String mapToEnglish(String thaiCategory) {
        switch (thaiCategory) {
            case "โปรตีน":
                return "Protein";
            case "ไขมัน":
                return "Fat";
            case "คาร์โบไฮเดรต":
                return "Carbohydrates";
            case "ผัก":
                return "Vegetables";
            case "อาหารจานเดียว":
                return "One-dish meals";
            case "เครื่องดื่ม":
                return "Beverages";
            default:
                return "Protein";
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("dashboard.fxml", 1200, 800);
    }
}
