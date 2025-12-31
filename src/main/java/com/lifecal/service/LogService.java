package com.lifecal.service;

import com.lifecal.model.ExerciseLog;
import com.lifecal.model.FoodLog;
import com.lifecal.model.WeightLog;
import com.lifecal.repository.ExerciseLogRepository;
import com.lifecal.repository.FoodLogRepository;
import com.lifecal.repository.WeightLogRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * LogService - Business logic for daily logging operations
 */
public class LogService {
    private FoodLogRepository foodLogRepository;
    private ExerciseLogRepository exerciseLogRepository;
    private WeightLogRepository weightLogRepository;

    public LogService() {
        this.foodLogRepository = new FoodLogRepository();
        this.exerciseLogRepository = new ExerciseLogRepository();
        this.weightLogRepository = new WeightLogRepository();
    }

    // Food Log Operations

    public boolean addFoodLog(FoodLog log) {
        return foodLogRepository.save(log);
    }

    public List<FoodLog> getFoodLogsByDate(int userId, LocalDate date) {
        return foodLogRepository.findByUserAndDate(userId, date);
    }

    public boolean deleteFoodLog(int id) {
        return foodLogRepository.delete(id);
    }

    public double getTotalCaloriesForDate(int userId, LocalDate date) {
        List<FoodLog> logs = getFoodLogsByDate(userId, date);
        return logs.stream().mapToDouble(FoodLog::getTotalCalories).sum();
    }

    // Exercise Log Operations

    public boolean addExerciseLog(ExerciseLog log) {
        return exerciseLogRepository.save(log);
    }

    public List<ExerciseLog> getExerciseLogsByDate(int userId, LocalDate date) {
        return exerciseLogRepository.findByUserAndDate(userId, date);
    }

    public boolean deleteExerciseLog(int id) {
        return exerciseLogRepository.delete(id);
    }

    public double getTotalCaloriesBurnedForDate(int userId, LocalDate date) {
        List<ExerciseLog> logs = getExerciseLogsByDate(userId, date);
        return logs.stream().mapToDouble(ExerciseLog::getCaloriesBurned).sum();
    }

    // Weight Log Operations

    public boolean addWeightLog(WeightLog log) {
        return weightLogRepository.save(log);
    }

    public List<WeightLog> getWeightHistory(int userId) {
        return weightLogRepository.findByUser(userId);
    }

    public WeightLog getWeightForDate(int userId, LocalDate date) {
        return weightLogRepository.findByUserAndDate(userId, date);
    }

    public boolean deleteWeightLog(int id) {
        return weightLogRepository.delete(id);
    }
}
