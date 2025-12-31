package com.lifecal.model;

import java.time.LocalDateTime;

/**
 * User - Model class representing a user in the system
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String email; // Email for password recovery
    private double currentWeight; // in kg
    private double height; // in cm
    private double targetWeight; // in kg
    private String activityLevel; // Very Fast, Fast, Normal, Slow
    private String exerciseFrequency; // 1-3, 4-5, 6-7
    private String profileImage;
    private double goalStartWeight; // Weight when the current goal was set
    private String role; // ADMIN or USER
    private LocalDateTime createdAt;

    // Constructors
    public User() {
    }

    public User(int id, String username, String passwordHash, String email, double currentWeight,
            double height, double targetWeight, String activityLevel,
            String exerciseFrequency, String profileImage, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.currentWeight = currentWeight;
        this.height = height;
        this.targetWeight = targetWeight;
        this.activityLevel = activityLevel;
        this.exerciseFrequency = exerciseFrequency;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(String exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public double getGoalStartWeight() {
        return goalStartWeight;
    }

    public void setGoalStartWeight(double goalStartWeight) {
        this.goalStartWeight = goalStartWeight;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", currentWeight=" + currentWeight +
                ", height=" + height +
                ", targetWeight=" + targetWeight +
                ", activityLevel='" + activityLevel + '\'' +
                ", exerciseFrequency='" + exerciseFrequency + '\'' +
                '}';
    }
}
