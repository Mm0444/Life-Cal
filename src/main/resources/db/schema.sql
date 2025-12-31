-- LIFE CAL System Database Schema
-- SQLite Database

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    email TEXT, -- Optional email for password recovery
    current_weight REAL NOT NULL,
    height REAL NOT NULL,
    target_weight REAL NOT NULL,
    activity_level TEXT NOT NULL, -- Very Fast, Fast, Normal, Slow
    exercise_frequency TEXT NOT NULL, -- 1-3, 4-5, 6-7
    goal_start_weight REAL DEFAULT 0,
    profile_image TEXT,
    role TEXT DEFAULT 'USER', -- ADMIN or USER
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Foods table
CREATE TABLE IF NOT EXISTS foods (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT NOT NULL, -- Protein, Fat, Carbohydrates, Vegetables, One-dish meals, Beverages
    kcal_per_serving REAL NOT NULL,
    protein REAL DEFAULT 0,
    carbs REAL DEFAULT 0,
    fat REAL DEFAULT 0
);

-- Exercises table
CREATE TABLE IF NOT EXISTS exercises (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT NOT NULL, -- Walking, Running, Conditioning Exercise, Sports, Dancing
    kcal_per_minute REAL NOT NULL
);

-- Food logs table
CREATE TABLE IF NOT EXISTS food_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    food_id INTEGER NOT NULL,
    date TEXT NOT NULL, -- Format: YYYY-MM-DD
    quantity REAL NOT NULL,
    total_calories REAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (food_id) REFERENCES foods(id)
);

-- Exercise logs table
CREATE TABLE IF NOT EXISTS exercise_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    date TEXT NOT NULL, -- Format: YYYY-MM-DD
    duration_minutes INTEGER NOT NULL,
    calories_burned REAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);

-- Weight logs table
CREATE TABLE IF NOT EXISTS weight_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    date TEXT NOT NULL, -- Format: YYYY-MM-DD
    weight REAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE(user_id, date) -- One weight entry per day per user
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_food_logs_user_date ON food_logs(user_id, date);
CREATE INDEX IF NOT EXISTS idx_exercise_logs_user_date ON exercise_logs(user_id, date);
CREATE INDEX IF NOT EXISTS idx_weight_logs_user_date ON weight_logs(user_id, date);
CREATE INDEX IF NOT EXISTS idx_foods_category ON foods(category);
CREATE INDEX IF NOT EXISTS idx_exercises_category ON exercises(category);
