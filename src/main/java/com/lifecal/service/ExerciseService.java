package com.lifecal.service;

import com.lifecal.model.Exercise;
import com.lifecal.repository.ExerciseRepository;

import java.util.List;

/**
 * ExerciseService - Business logic for exercise operations
 */
public class ExerciseService {
    private ExerciseRepository exerciseRepository;

    public ExerciseService() {
        this.exerciseRepository = new ExerciseRepository();
    }

    /**
     * Get all exercises
     */
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    /**
     * Get exercises by category
     */
    public List<Exercise> getExercisesByCategory(String category) {
        return exerciseRepository.findByCategory(category);
    }

    /**
     * Get exercise by ID
     */
    public Exercise getExerciseById(int id) {
        return exerciseRepository.findById(id);
    }

    /**
     * Calculate calories burned for an exercise
     */
    public double calculateCaloriesBurned(Exercise exercise, int durationMinutes) {
        return HealthCalculator.calculateCaloriesBurned(exercise.getKcalPerMinute(), durationMinutes);
    }

    /**
     * Get exercise categories
     */
    public String[] getExerciseCategories() {
        return new String[] {
                "Walking",
                "Running",
                "Conditioning Exercise",
                "Sports",
                "Dancing"
        };
    }
}
