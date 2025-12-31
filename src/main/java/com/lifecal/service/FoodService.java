package com.lifecal.service;

import com.lifecal.model.Food;
import com.lifecal.repository.FoodRepository;

import java.util.List;

/**
 * FoodService - Business logic for food operations
 */
public class FoodService {
    private FoodRepository foodRepository;

    public FoodService() {
        this.foodRepository = new FoodRepository();
    }

    /**
     * Get all foods
     */
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    /**
     * Get foods by category
     */
    public List<Food> getFoodsByCategory(String category) {
        return foodRepository.findByCategory(category);
    }

    /**
     * Get food by ID
     */
    public Food getFoodById(int id) {
        return foodRepository.findById(id);
    }

    /**
     * Get food categories
     */
    public String[] getFoodCategories() {
        return new String[] {
                "Protein",
                "Fat",
                "Carbohydrates",
                "Vegetables",
                "One-dish meals",
                "Beverages"
        };
    }
}
