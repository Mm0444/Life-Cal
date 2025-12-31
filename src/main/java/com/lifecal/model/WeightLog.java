package com.lifecal.model;

import java.time.LocalDate;

/**
 * WeightLog - Model class representing a weight entry
 */
public class WeightLog {
    private int id;
    private int userId;
    private LocalDate date;
    private double weight; // in kg

    // Constructors
    public WeightLog() {
    }

    public WeightLog(int id, int userId, LocalDate date, double weight) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.weight = weight;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WeightLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", weight=" + weight +
                '}';
    }
}
