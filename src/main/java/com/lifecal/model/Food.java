package com.lifecal.model;

/**
 * Food - Model class representing a food item
 */
public class Food {
    private int id;
    private String name;
    private String category; // Protein, Fat, Carbohydrates, Vegetables, One-dish meals, Beverages
    private double kcalPerServing;
    private double protein; // grams
    private double carbs; // grams
    private double fat; // grams

    // Constructors
    public Food() {
    }

    public Food(int id, String name, String category, double kcalPerServing,
            double protein, double carbs, double fat) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.kcalPerServing = kcalPerServing;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getKcalPerServing() {
        return kcalPerServing;
    }

    public void setKcalPerServing(double kcalPerServing) {
        this.kcalPerServing = kcalPerServing;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", kcalPerServing=" + kcalPerServing +
                '}';
    }
}
