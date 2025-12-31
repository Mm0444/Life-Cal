package com.lifecal.service;

import com.lifecal.model.User;
import com.lifecal.repository.UserRepository;

import java.util.List;

/**
 * AdminService - Business logic for admin operations
 */
public class AdminService {
    private UserRepository userRepository;

    public AdminService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Get all users in the system
     */
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    /**
     * Delete a user and all their data
     * 
     * @param userId ID of the user to delete
     * @return true if deletion was successful
     */
    public boolean deleteUser(int userId) {
        return userRepository.deleteUser(userId);
    }

    /**
     * Get total user count
     */
    public int getUserCount() {
        return getAllUsers().size();
    }

    /**
     * Check if user can be deleted (prevent deleting yourself)
     */
    public boolean canDeleteUser(int userIdToDelete, int currentUserId) {
        return userIdToDelete != currentUserId;
    }

    // Food Management
    private com.lifecal.repository.FoodRepository foodRepository = new com.lifecal.repository.FoodRepository();

    public List<com.lifecal.model.Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public boolean addFood(com.lifecal.model.Food food) {
        return foodRepository.save(food);
    }

    // Exercise Management
    private com.lifecal.repository.ExerciseRepository exerciseRepository = new com.lifecal.repository.ExerciseRepository();

    public List<com.lifecal.model.Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public boolean addExercise(com.lifecal.model.Exercise exercise) {
        return exerciseRepository.save(exercise);
    }
}
