package com.lifecal.service;

import com.lifecal.model.WeightLog;
import com.lifecal.repository.ExerciseLogRepository;
import com.lifecal.repository.FoodLogRepository;
import com.lifecal.repository.WeightLogRepository;

import java.time.LocalDate;
import java.util.*;

/**
 * ReportService - Business logic for generating statistics and reports
 */
public class ReportService {
    private FoodLogRepository foodLogRepository;
    private ExerciseLogRepository exerciseLogRepository;
    private WeightLogRepository weightLogRepository;

    public ReportService() {
        this.foodLogRepository = new FoodLogRepository();
        this.exerciseLogRepository = new ExerciseLogRepository();
        this.weightLogRepository = new WeightLogRepository();
    }

    /**
     * Get calorie statistics for a date range
     * Returns a map with keys: "intake", "burned", "net"
     * Each maps to another map of LocalDate -> calories
     */
    public Map<String, Object> getCalorieStatistics(int userId, LocalDate start, LocalDate end) {
        Map<String, Object> stats = new HashMap<>();

        // Get intake calories
        Map<LocalDate, Double> intakeByDate = foodLogRepository.getTotalCaloriesByDateRange(userId, start, end);

        // Get burned calories
        Map<LocalDate, Double> burnedByDate = exerciseLogRepository.getTotalCaloriesBurnedByDateRange(userId, start,
                end);

        // Calculate net calories (intake - burned)
        Map<LocalDate, Double> netByDate = new HashMap<>();
        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(intakeByDate.keySet());
        allDates.addAll(burnedByDate.keySet());

        for (LocalDate date : allDates) {
            double intake = intakeByDate.getOrDefault(date, 0.0);
            double burned = burnedByDate.getOrDefault(date, 0.0);
            netByDate.put(date, intake - burned);
        }

        stats.put("intake", intakeByDate);
        stats.put("burned", burnedByDate);
        stats.put("net", netByDate);

        // Calculate totals
        double totalIntake = intakeByDate.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalBurned = burnedByDate.values().stream().mapToDouble(Double::doubleValue).sum();

        stats.put("totalIntake", totalIntake);
        stats.put("totalBurned", totalBurned);
        stats.put("totalNet", totalIntake - totalBurned);

        return stats;
    }

    /**
     * Get weight trend data for charting
     */
    public List<WeightLog> getWeightTrend(int userId) {
        return weightLogRepository.findByUser(userId);
    }

    /**
     * Get weight statistics
     */
    public Map<String, Object> getWeightStatistics(int userId) {
        Map<String, Object> stats = new HashMap<>();
        List<WeightLog> weightHistory = weightLogRepository.findByUser(userId);

        if (!weightHistory.isEmpty()) {
            // Get first and latest weight
            WeightLog firstEntry = weightHistory.get(0);
            WeightLog latestEntry = weightHistory.get(weightHistory.size() - 1);

            stats.put("startWeight", firstEntry.getWeight());
            stats.put("currentWeight", latestEntry.getWeight());
            stats.put("weightChange", latestEntry.getWeight() - firstEntry.getWeight());
            stats.put("startDate", firstEntry.getDate());
            stats.put("latestDate", latestEntry.getDate());
        }

        return stats;
    }
}
