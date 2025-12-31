package com.lifecal.model;

import java.time.LocalDate;

/**
 * FoodLog - Model class representing a daily food intake entry
 */
public class FoodLog {
    private int id;
    private int userId;
    private int foodId;
    private LocalDate date;
    private double quantity; // servings/portions
    private double totalCalories;

    // For display purposes (not stored in DB)
    private String foodName;
    private String mealTime;

    // Constructors
    public FoodLog() {
    }

    public FoodLog(int id, int userId, int foodId, LocalDate date,
            double quantity, double totalCalories, String mealTime) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.date = date;
        this.quantity = quantity;
        this.totalCalories = totalCalories;
        this.mealTime = mealTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    @Override
    public String toString() {
        return "FoodLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", foodId=" + foodId +
                ", date=" + date +
                ", quantity=" + quantity +
                ", totalCalories=" + totalCalories +
                '}';
    }
}
