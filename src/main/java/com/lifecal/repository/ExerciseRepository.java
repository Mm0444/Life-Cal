package com.lifecal.repository;

import com.lifecal.common.DatabaseManager;
import com.lifecal.model.Exercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ExerciseRepository - Data Access Object for Exercise entity
 */
public class ExerciseRepository {
    private Connection connection;

    public ExerciseRepository() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Find all exercises
     */
    public List<Exercise> findAll() {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises ORDER BY category, name";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                exercises.add(extractExerciseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    /**
     * Find exercises by category
     */
    public List<Exercise> findByCategory(String category) {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE category = ? ORDER BY name";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                exercises.add(extractExerciseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    /**
     * Find exercise by ID
     */
    public Exercise findById(int id) {
        String sql = "SELECT * FROM exercises WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractExerciseFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save new exercise
     */
    public boolean save(Exercise exercise) {
        String sql = "INSERT INTO exercises (name, category, kcal_per_minute) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, exercise.getName());
            pstmt.setString(2, exercise.getCategory());
            pstmt.setDouble(3, exercise.getKcalPerMinute());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    exercise.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Extract Exercise object from ResultSet
     */
    private Exercise extractExerciseFromResultSet(ResultSet rs) throws SQLException {
        Exercise exercise = new Exercise();
        exercise.setId(rs.getInt("id"));
        exercise.setName(rs.getString("name"));
        exercise.setCategory(rs.getString("category"));
        exercise.setKcalPerMinute(rs.getDouble("kcal_per_minute"));
        return exercise;
    }
}
