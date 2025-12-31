package com.lifecal.service;

/**
 * HealthCalculator - Service class for health-related calculations
 * Includes BMI, BMR, and calorie calculations
 */
public class HealthCalculator {

    /**
     * Calculate BMI (Body Mass Index)
     * Formula: BMI = weight(kg) / (height(m))^2
     * 
     * @param weight Weight in kilograms
     * @param height Height in centimeters
     * @return BMI value
     */
    public static double calculateBMI(double weight, double height) {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    /**
     * Get BMI category
     */
    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    /**
     * Calculate BMR (Basal Metabolic Rate) using Mifflin-St Jeor Equation
     * For men: BMR = 10W + 6.25H - 5A + 5
     * For women: BMR = 10W + 6.25H - 5A - 161
     * Where W = weight in kg, H = height in cm, A = age in years
     * 
     * Since we don't have age/gender in the system, we'll use a simplified approach
     * based on weight and height only (approximate for average adult)
     * 
     * @param weight Weight in kilograms
     * @param height Height in centimeters
     * @return BMR (calories per day)
     */
    public static double calculateBMR(double weight, double height) {
        // Using simplified Harris-Benedict equation (average of male/female)
        // This gives an approximate BMR for adults aged 25-35
        return 10 * weight + 6.25 * height - 161;
    }

    /**
     * Calculate TDEE (Total Daily Energy Expenditure) based on activity level
     * 
     * @param bmr           Basal Metabolic Rate
     * @param activityLevel Activity level (Very Fast, Fast, Normal, Slow)
     * @return TDEE (calories per day)
     */
    public static double calculateTDEE(double bmr, String activityLevel) {
        double multiplier;
        switch (activityLevel) {
            case "Very Fast":
                multiplier = 1.9; // Very active (hard exercise 6-7 days/week)
                break;
            case "Fast":
                multiplier = 1.725; // Very active (moderate exercise 6-7 days/week)
                break;
            case "Normal":
                multiplier = 1.55; // Moderately active (moderate exercise 3-5 days/week)
                break;
            case "Slow":
                multiplier = 1.375; // Lightly active (light exercise 1-3 days/week)
                break;
            default:
                multiplier = 1.55; // Default to normal
        }
        return bmr * multiplier;
    }

    /**
     * Calculate calories burned during exercise
     * 
     * @param kcalPerMinute   Calories burned per minute for the exercise
     * @param durationMinutes Duration in minutes
     * @return Total calories burned
     */
    public static double calculateCaloriesBurned(double kcalPerMinute, int durationMinutes) {
        return kcalPerMinute * durationMinutes;
    }

    /**
     * Calculate daily calorie goal based on target weight
     * 
     * @param currentWeight Current weight in kg
     * @param targetWeight  Target weight in kg
     * @param tdee          Total Daily Energy Expenditure
     * @return Recommended daily calorie intake
     */
    public static double calculateDailyCalorieGoal(double currentWeight, double targetWeight, double tdee) {
        if (targetWeight < currentWeight) {
            // Weight loss: reduce calories by 500 per day (lose ~0.5kg per week)
            return tdee - 500;
        } else if (targetWeight > currentWeight) {
            // Weight gain: increase calories by 500 per day (gain ~0.5kg per week)
            return tdee + 500;
        } else {
            // Maintain weight
            return tdee;
        }
    }

    /**
     * Calculate weight progress percentage
     * 
     * @param startWeight   Starting weight
     * @param currentWeight Current weight
     * @param targetWeight  Target weight
     * @return Progress percentage (0-100)
     */
    public static double calculateWeightProgress(double startWeight, double currentWeight, double targetWeight) {
        if (Math.abs(targetWeight - startWeight) < 0.1) {
            return 100.0; // Already at target if difference is negligible
        }

        double totalChange = targetWeight - startWeight;
        double currentChange = currentWeight - startWeight;

        // Avoid division by zero
        if (Math.abs(totalChange) < 0.1) {
            return 100.0;
        }

        double progress = (currentChange / totalChange) * 100.0;

        // If progress is negative (moving away from goal), return 0
        return Math.max(0.0, progress);
    }
}
