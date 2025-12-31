package com.lifecal.repository;

import com.lifecal.common.DatabaseManager;
import com.lifecal.model.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FoodRepository - Data Access Object for Food entity
 */
public class FoodRepository {
    private Connection connection;

    public FoodRepository() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Find all foods
     */
    public List<Food> findAll() {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods ORDER BY category, name";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                foods.add(extractFoodFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    /**
     * Find foods by category
     */
    public List<Food> findByCategory(String category) {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods WHERE category = ? ORDER BY name";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                foods.add(extractFoodFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    /**
     * Find food by ID
     */
    public Food findById(int id) {
        String sql = "SELECT * FROM foods WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractFoodFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save new food
     */
    public boolean save(Food food) {
        String sql = "INSERT INTO foods (name, category, kcal_per_serving, protein, carbs, fat) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, food.getName());
            pstmt.setString(2, food.getCategory());
            pstmt.setDouble(3, food.getKcalPerServing());
            pstmt.setDouble(4, food.getProtein());
            pstmt.setDouble(5, food.getCarbs());
            pstmt.setDouble(6, food.getFat());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    food.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Extract Food object from ResultSet
     */
    private Food extractFoodFromResultSet(ResultSet rs) throws SQLException {
        Food food = new Food();
        food.setId(rs.getInt("id"));
        food.setName(rs.getString("name"));
        food.setCategory(rs.getString("category"));
        food.setKcalPerServing(rs.getDouble("kcal_per_serving"));
        food.setProtein(rs.getDouble("protein"));
        food.setCarbs(rs.getDouble("carbs"));
        food.setFat(rs.getDouble("fat"));
        return food;
    }
}
