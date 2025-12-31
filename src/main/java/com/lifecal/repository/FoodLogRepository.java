package com.lifecal.repository;

import com.lifecal.common.DatabaseManager;
import com.lifecal.model.FoodLog;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FoodLogRepository - Data Access Object for FoodLog entity
 */
public class FoodLogRepository {
    private Connection connection;

    public FoodLogRepository() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Find food logs by user and date
     */
    public List<FoodLog> findByUserAndDate(int userId, LocalDate date) {
        List<FoodLog> logs = new ArrayList<>();
        String sql = "SELECT fl.*, f.name as food_name FROM food_logs fl " +
                "JOIN foods f ON fl.food_id = f.id " +
                "WHERE fl.user_id = ? AND fl.date = ? ORDER BY fl.id";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, date.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FoodLog log = extractFoodLogFromResultSet(rs);
                log.setFoodName(rs.getString("food_name"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Save new food log
     */
    public boolean save(FoodLog log) {
        String sql = "INSERT INTO food_logs (user_id, food_id, date, quantity, total_calories, meal_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, log.getUserId());
            pstmt.setInt(2, log.getFoodId());
            pstmt.setString(3, log.getDate().toString());
            pstmt.setDouble(4, log.getQuantity());
            pstmt.setDouble(5, log.getTotalCalories());
            pstmt.setString(6, log.getMealTime());

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
     * Delete food log by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM food_logs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get total calories by date range
     */
    public Map<LocalDate, Double> getTotalCaloriesByDateRange(int userId, LocalDate start, LocalDate end) {
        Map<LocalDate, Double> caloriesByDate = new HashMap<>();
        String sql = "SELECT date, SUM(total_calories) as total FROM food_logs " +
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
     * Extract FoodLog object from ResultSet
     */
    private FoodLog extractFoodLogFromResultSet(ResultSet rs) throws SQLException {
        FoodLog log = new FoodLog();
        log.setId(rs.getInt("id"));
        log.setUserId(rs.getInt("user_id"));
        log.setFoodId(rs.getInt("food_id"));
        log.setDate(LocalDate.parse(rs.getString("date")));
        log.setQuantity(rs.getDouble("quantity"));
        log.setTotalCalories(rs.getDouble("total_calories"));
        log.setMealTime(rs.getString("meal_time"));
        return log;
    }
}
