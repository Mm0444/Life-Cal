package com.lifecal.repository;

import com.lifecal.common.DatabaseManager;
import com.lifecal.model.ExerciseLog;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExerciseLogRepository - Data Access Object for ExerciseLog entity
 */
public class ExerciseLogRepository {
    private Connection connection;

    public ExerciseLogRepository() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Find exercise logs by user and date
     */
    public List<ExerciseLog> findByUserAndDate(int userId, LocalDate date) {
        List<ExerciseLog> logs = new ArrayList<>();
        String sql = "SELECT el.*, e.name as exercise_name FROM exercise_logs el " +
                "JOIN exercises e ON el.exercise_id = e.id " +
                "WHERE el.user_id = ? AND el.date = ? ORDER BY el.id";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, date.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ExerciseLog log = extractExerciseLogFromResultSet(rs);
                log.setExerciseName(rs.getString("exercise_name"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Save new exercise log
     */
    public boolean save(ExerciseLog log) {
        String sql = "INSERT INTO exercise_logs (user_id, exercise_id, date, duration_minutes, calories_burned) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, log.getUserId());
            pstmt.setInt(2, log.getExerciseId());
            pstmt.setString(3, log.getDate().toString());
            pstmt.setInt(4, log.getDurationMinutes());
            pstmt.setDouble(5, log.getCaloriesBurned());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    log.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete exercise log by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM exercise_logs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get total calories burned by date range
     */
    public Map<LocalDate, Double> getTotalCaloriesBurnedByDateRange(int userId, LocalDate start, LocalDate end) {
        Map<LocalDate, Double> caloriesByDate = new HashMap<>();
        String sql = "SELECT date, SUM(calories_burned) as total FROM exercise_logs " +
                "WHERE user_id = ? AND date >= ? AND date <= ? " +
                "GROUP BY date ORDER BY date";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, start.toString());
            pstmt.setString(3, end.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                double total = rs.getDouble("total");
                caloriesByDate.put(date, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caloriesByDate;
    }

    /**
     * Extract ExerciseLog object from ResultSet
     */
    private ExerciseLog extractExerciseLogFromResultSet(ResultSet rs) throws SQLException {
        ExerciseLog log = new ExerciseLog();
        log.setId(rs.getInt("id"));
        log.setUserId(rs.getInt("user_id"));
        log.setExerciseId(rs.getInt("exercise_id"));
        log.setDate(LocalDate.parse(rs.getString("date")));
        log.setDurationMinutes(rs.getInt("duration_minutes"));
        log.setCaloriesBurned(rs.getDouble("calories_burned"));
        return log;
    }
}
