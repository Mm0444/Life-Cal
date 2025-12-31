package com.lifecal.model;

import java.time.LocalDate;

/**
 * ExerciseLog - Model class representing a daily exercise entry
 */
public class ExerciseLog {
    private int id;
    private int userId;
    private int exerciseId;
    private LocalDate date;
    private int durationMinutes;
    private double caloriesBurned;

    // For display purposes (not stored in DB)
    private String exerciseName;

    // Constructors
    public ExerciseLog() {
    }

    public ExerciseLog(int id, int userId, int exerciseId, LocalDate date,
            int durationMinutes, double caloriesBurned) {
        this.id = id;
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
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

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    @Override
    public String toString() {
        return "ExerciseLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", exerciseId=" + exerciseId +
                ", date=" + date +
                ", durationMinutes=" + durationMinutes +
                ", caloriesBurned=" + caloriesBurned +
                '}';
    }
}
