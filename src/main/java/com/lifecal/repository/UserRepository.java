package com.lifecal.repository;

import com.lifecal.common.DatabaseManager;
import com.lifecal.model.User;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * UserRepository - Data Access Object for User entity
 */
public class UserRepository {
    private Connection connection;

    public UserRepository() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if username already exists
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if email already exists (excluding the current user)
     */
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false; // Empty email is allowed
        }
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if email already exists for a different user
     */
    public boolean existsByEmailExcludingUser(String email, int userId) {
        if (email == null || email.trim().isEmpty()) {
            return false; // Empty email is allowed
        }
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Save new user to database
     */
    public boolean save(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, current_weight, height, " +
                "target_weight, activity_level, exercise_frequency, profile_image, goal_start_weight, role, created_at) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getEmail());
            pstmt.setDouble(4, user.getCurrentWeight());
            pstmt.setDouble(5, user.getHeight());
            pstmt.setDouble(6, user.getTargetWeight());
            pstmt.setString(7, user.getActivityLevel());
            pstmt.setString(8, user.getExerciseFrequency());
            pstmt.setString(9, user.getProfileImage());
            // Default goal start weight to current weight for new users
            pstmt.setDouble(10, user.getCurrentWeight());
            pstmt.setString(11, user.getRole() != null ? user.getRole() : "USER");
            pstmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update existing user
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, current_weight = ?, height = ?, " +
                "target_weight = ?, activity_level = ?, exercise_frequency = ?, " +
                "profile_image = ?, goal_start_weight = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setDouble(3, user.getCurrentWeight());
            pstmt.setDouble(4, user.getHeight());
            pstmt.setDouble(5, user.getTargetWeight());
            pstmt.setString(6, user.getActivityLevel());
            pstmt.setString(7, user.getExerciseFrequency());
            pstmt.setString(8, user.getProfileImage());
            pstmt.setDouble(9, user.getGoalStartWeight());
            pstmt.setInt(10, user.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Find user by ID
     */
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extract User object from ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setCurrentWeight(rs.getDouble("current_weight"));
        user.setHeight(rs.getDouble("height"));
        user.setTargetWeight(rs.getDouble("target_weight"));
        user.setActivityLevel(rs.getString("activity_level"));
        user.setExerciseFrequency(rs.getString("exercise_frequency"));
        user.setProfileImage(rs.getString("profile_image"));
        user.setGoalStartWeight(rs.getDouble("goal_start_weight"));
        user.setRole(rs.getString("role"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            user.setCreatedAt(timestamp.toLocalDateTime());
        }

        return user;
    }

    /**
     * Get all users (for admin panel)
     */
    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Delete user and all related data (cascading delete)
     */
    public boolean deleteUser(int userId) {
        try {
            // Delete in order: logs first, then user
            connection.setAutoCommit(false);

            // Delete food logs
            String deleteFoodLogs = "DELETE FROM food_logs WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteFoodLogs)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }

            // Delete exercise logs
            String deleteExerciseLogs = "DELETE FROM exercise_logs WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteExerciseLogs)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }

            // Delete weight logs
            String deleteWeightLogs = "DELETE FROM weight_logs WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteWeightLogs)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }

            // Finally delete user
            String deleteUser = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteUser)) {
                pstmt.setInt(1, userId);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
