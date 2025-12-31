package com.lifecal.model;

/**
 * Exercise - Model class representing an exercise
 */
public class Exercise {
    private int id;
    private String name;
    private String category; // Walking, Running, Conditioning Exercise, Sports, Dancing
    private double kcalPerMinute;

    // Constructors
    public Exercise() {
    }

    public Exercise(int id, String name, String category, double kcalPerMinute) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.kcalPerMinute = kcalPerMinute;
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

    public double getKcalPerMinute() {
        return kcalPerMinute;
    }

    public void setKcalPerMinute(double kcalPerMinute) {
        this.kcalPerMinute = kcalPerMinute;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", kcalPerMinute=" + kcalPerMinute +
                '}';
    }
}
