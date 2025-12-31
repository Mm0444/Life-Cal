package com.lifecal.repository;

import com.lifecal.common.DatabaseManager;
import com.lifecal.model.WeightLog;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * WeightLogRepository - Data Access Object for WeightLog entity
 */
public class WeightLogRepository {
    private Connection connection;

    public WeightLogRepository() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Find all weight logs for a user
     */
    public List<WeightLog> findByUser(int userId) {
        List<WeightLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM weight_logs WHERE user_id = ? ORDER BY date ASC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(extractWeightLogFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Find weight log by user and date
     */
    public WeightLog findByUserAndDate(int userId, LocalDate date) {
        String sql = "SELECT * FROM weight_logs WHERE user_id = ? AND date = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, date.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractWeightLogFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save new weight log (or update if exists for that date)
     */
    public boolean save(WeightLog log) {
        // Check if entry exists for this date
        WeightLog existing = findByUserAndDate(log.getUserId(), log.getDate());

        if (existing != null) {
            // Update existing entry
            String sql = "UPDATE weight_logs SET weight = ? WHERE user_id = ? AND date = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setDouble(1, log.getWeight());
                pstmt.setInt(2, log.getUserId());
                pstmt.setString(3, log.getDate().toString());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Insert new entry
            String sql = "INSERT INTO weight_logs (user_id, date, weight) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, log.getUserId());
                pstmt.setString(2, log.getDate().toString());
                pstmt.setDouble(3, log.getWeight());

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
        }
        return false;
    }

    /**
     * Delete weight log by ID
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM weight_logs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract WeightLog object from ResultSet
     */
    private WeightLog extractWeightLogFromResultSet(ResultSet rs) throws SQLException {
        WeightLog log = new WeightLog();
        log.setId(rs.getInt("id"));
        log.setUserId(rs.getInt("user_id"));
        log.setDate(LocalDate.parse(rs.getString("date")));
        log.setWeight(rs.getDouble("weight"));
        return log;
    }
}
