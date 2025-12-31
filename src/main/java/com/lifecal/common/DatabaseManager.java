package com.lifecal.common;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

/**
 * DatabaseManager - Singleton class for managing SQLite database connection
 * Handles initialization, schema creation, and data seeding
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_NAME = "lifecal.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    /**
     * Private constructor for singleton pattern
     */
    private DatabaseManager() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish connection
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established: " + DB_NAME);

            // Initialize database schema
            initializeSchema();

            // Seed data if tables are empty
            seedDataIfNeeded();

        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            e.printStackTrace();
        }
    }

    /**
     * Get singleton instance of DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Get database connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Initialize database schema from schema.sql file
     */
    private void initializeSchema() {
        try {
            // Read schema.sql from resources
            InputStream schemaStream = getClass().getClassLoader()
                    .getResourceAsStream("db/schema.sql");

            if (schemaStream == null) {
                System.err.println("schema.sql not found in resources!");
                return;
            }

            Scanner scanner = new Scanner(schemaStream).useDelimiter(";");
            Statement statement = connection.createStatement();

            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (!sql.isEmpty()) {
                    statement.execute(sql);
                }
            }

            scanner.close();
            statement.close();
            System.out.println("Database schema initialized successfully");

            // Run migrations
            checkAndMigrateSchema();

        } catch (SQLException e) {
            System.err.println("Failed to initialize database schema!");
            e.printStackTrace();
        }
    }

    /**
     * Check and migrate database schema for updates
     */
    private void checkAndMigrateSchema() {
        try (Statement stmt = connection.createStatement()) {
            // Check if goal_start_weight column exists in users table
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(users)");
            boolean hasGoalStartWeight = false;
            boolean hasRole = false;

            while (rs.next()) {
                String columnName = rs.getString("name");
                if ("goal_start_weight".equals(columnName)) {
                    hasGoalStartWeight = true;
                } else if ("role".equals(columnName)) {
                    hasRole = true;
                }
            }
            rs.close();

            if (!hasGoalStartWeight) {
                System.out.println("Migrating schema: Adding goal_start_weight to users table...");
                stmt.execute("ALTER TABLE users ADD COLUMN goal_start_weight REAL DEFAULT 0");

                // Update existing users to set goal_start_weight = current_weight (best guess)
                stmt.execute("UPDATE users SET goal_start_weight = current_weight");
                System.out.println("Migration completed successfully.");
            }

            if (!hasRole) {
                System.out.println("Migrating schema: Adding role to users table...");
                stmt.execute("ALTER TABLE users ADD COLUMN role TEXT DEFAULT 'USER'");
                System.out.println("Role column added successfully.");
            }

            // Check if meal_time column exists in food_logs table
            rs = stmt.executeQuery("PRAGMA table_info(food_logs)");
            boolean hasMealTime = false;
            while (rs.next()) {
                if ("meal_time".equals(rs.getString("name"))) {
                    hasMealTime = true;
                    break;
                }
            }
            rs.close();

            if (!hasMealTime) {
                System.out.println("Migrating schema: Adding meal_time to food_logs table...");
                stmt.execute("ALTER TABLE food_logs ADD COLUMN meal_time TEXT");
                System.out.println("Meal time column added successfully.");
            }

            // Create default admin account if it doesn't exist
            createDefaultAdminAccount();

        } catch (SQLException e) {
            System.err.println("Failed to migrate database schema!");
            e.printStackTrace();
        }
    }

    /**
     * Create default admin account if not exists
     */
    private void createDefaultAdminAccount() {
        try (Statement stmt = connection.createStatement()) {
            // Check if admin user already exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'admin'");
            int adminCount = rs.getInt(1);
            rs.close();

            if (adminCount == 0) {
                System.out.println("Creating default admin account...");

                // Hash password (admin123)
                String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw("admin123",
                        org.mindrot.jbcrypt.BCrypt.gensalt());

                String sql = "INSERT INTO users (username, password_hash, current_weight, height, " +
                        "target_weight, activity_level, exercise_frequency, goal_start_weight, role) " +
                        "VALUES ('admin', ?, 70.0, 170.0, 70.0, 'Normal', '4-5', 70.0, 'ADMIN')";

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, hashedPassword);
                    pstmt.executeUpdate();
                    System.out.println("Default admin account created successfully.");
                    System.out.println("Username: admin, Password: admin123");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to create default admin account!");
            e.printStackTrace();
        }
    }

    /**
     * Seed initial food and exercise data if tables are empty
     */
    private void seedDataIfNeeded() {
        try {
            // Check if foods table is empty
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM foods");
            int foodCount = rs.getInt(1);
            rs.close();

            if (foodCount == 0) {
                seedFoods();
                System.out.println("Sample food data seeded");
            }

            // Check if exercises table is empty
            rs = stmt.executeQuery("SELECT COUNT(*) FROM exercises");
            int exerciseCount = rs.getInt(1);
            rs.close();

            if (exerciseCount == 0) {
                seedExercises();
                System.out.println("Sample exercise data seeded");
            }

            stmt.close();

        } catch (SQLException e) {
            System.err.println("Failed to check/seed data!");
            e.printStackTrace();
        }
    }

    /**
     * Seed sample food data
     */
    private void seedFoods() throws SQLException {
        String sql = "INSERT INTO foods (name, category, kcal_per_serving, protein, carbs, fat) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        // Protein Foods (โปรตีน)
        insertFood(pstmt, "ไก่อกไม่มีหนัง (100 กรัม)", "Protein", 165, 31, 0, 3.6);
        insertFood(pstmt, "ปลาแซลมอน (100 กรัม)", "Protein", 208, 20, 0, 13);
        insertFood(pstmt, "ไข่ไก่ (2 ฟอง)", "Protein", 140, 12, 1, 10);
        insertFood(pstmt, "เต้าหู้ (100 กรัม)", "Protein", 76, 8, 1.9, 4.8);
        insertFood(pstmt, "โยเกิร์ตกรีก (100 กรัม)", "Protein", 59, 10, 3.6, 0.4);
        insertFood(pstmt, "ปลาทูน่ากระป๋อง (100 กรัม)", "Protein", 144, 30, 0, 1);
        insertFood(pstmt, "หมูสันใน (100 กรัม)", "Protein", 143, 21, 0, 6);
        insertFood(pstmt, "เนื้อวัวติดมัน (100 กรัม)", "Protein", 250, 26, 0, 15);
        insertFood(pstmt, "กุ้ง (100 กรัม)", "Protein", 99, 24, 0.2, 0.3);
        insertFood(pstmt, "ถั่วลิสง (30 กรัม)", "Protein", 161, 7, 5, 14);

        // Fat Foods (ไขมัน)
        insertFood(pstmt, "อะโวคาโด (100 กรัม)", "Fat", 160, 2, 9, 15);
        insertFood(pstmt, "อัลมอนด์ (30 กรัม)", "Fat", 170, 6, 6, 15);
        insertFood(pstmt, "น้ำมันมะกอก (1 ช้อนโต๊ะ)", "Fat", 119, 0, 0, 13.5);
        insertFood(pstmt, "เนยถั่ว (2 ช้อนโต๊ะ)", "Fat", 188, 8, 7, 16);
        insertFood(pstmt, "เม็ดมะม่วงหิมพานต์ (30 กรัม)", "Fat", 157, 5, 9, 12);
        insertFood(pstmt, "เนยแข็ง (30 กรัม)", "Fat", 114, 7, 0.4, 9);
        insertFood(pstmt, "นมสด (1 แก้ว)", "Fat", 149, 8, 12, 8);

        // Carbohydrates/Grains (คาร์โบไฮเดรต)
        insertFood(pstmt, "ข้าวกล้อง (1 ถ้วย)", "Carbohydrates", 216, 5, 45, 1.8);
        insertFood(pstmt, "ขนมปังโฮลวีต (2 แผ่น)", "Carbohydrates", 160, 8, 28, 2);
        insertFood(pstmt, "โอ๊ตมีล (1 ถ้วย)", "Carbohydrates", 158, 6, 27, 3);
        insertFood(pstmt, "ควินัว (1 ถ้วย)", "Carbohydrates", 222, 8, 39, 3.6);
        insertFood(pstmt, "มันเทศ (100 กรัม)", "Carbohydrates", 86, 1.6, 20, 0.1);
        insertFood(pstmt, "ข้าวขาว (1 ถ้วย)", "Carbohydrates", 205, 4.3, 45, 0.4);
        insertFood(pstmt, "ข้าวโพด (1 ฝัก)", "Carbohydrates", 90, 3, 19, 1.4);
        insertFood(pstmt, "ก๋วยเตี๋ยวแห้ง (100 กรัม)", "Carbohydrates", 351, 11, 73, 1.4);
        insertFood(pstmt, "ขนมปังขาว (2 แผ่น)", "Carbohydrates", 150, 5, 28, 2);
        insertFood(pstmt, "มันฝรั่ง (100 กรัม)", "Carbohydrates", 77, 2, 17, 0.1);

        // Vegetables (ผัก)
        insertFood(pstmt, "บร็อคโคลี (100 กรัม)", "Vegetables", 34, 2.8, 7, 0.4);
        insertFood(pstmt, "ผักโขม (100 กรัม)", "Vegetables", 23, 2.9, 3.6, 0.4);
        insertFood(pstmt, "แครอท (100 กรัม)", "Vegetables", 41, 0.9, 10, 0.2);
        insertFood(pstmt, "มะเขือเทศ (100 กรัม)", "Vegetables", 18, 0.9, 3.9, 0.2);
        insertFood(pstmt, "พริกหยวก (100 กรัม)", "Vegetables", 31, 1, 6, 0.3);
        insertFood(pstmt, "กะหล่ำปลี (100 กรัม)", "Vegetables", 25, 1.3, 6, 0.1);
        insertFood(pstmt, "ผักกาดขาว (100 กรัม)", "Vegetables", 13, 1.5, 2.2, 0.2);
        insertFood(pstmt, "แตงกวา (100 กรัม)", "Vegetables", 16, 0.7, 3.6, 0.1);
        insertFood(pstmt, "ผักบุ้งจีน (100กรัม)", "Vegetables", 19, 2.6, 2.9, 0.2);
        insertFood(pstmt, "ถั่วงอก (100 กรัม)", "Vegetables", 30, 3, 6, 0.2);

        // One-dish Meals (อาหารจานเดียว)
        insertFood(pstmt, "ข้าวผัดไก่ (1 จาน)", "One-dish meals", 520, 25, 65, 15);
        insertFood(pstmt, "ข้าวผัดกุ้ง (1 จาน)", "One-dish meals", 480, 20, 60, 16);
        insertFood(pstmt, "ข้าวผัดหมู (1 จาน)", "One-dish meals", 540, 23, 68, 18);
        insertFood(pstmt, "ผัดไทย (1 จาน)", "One-dish meals", 450, 15, 56, 18);
        insertFood(pstmt, "ผัดซีอิ๊ว (1 จาน)", "One-dish meals", 420, 18, 52, 16);
        insertFood(pstmt, "ข้าวหมูแดง (1 จาน)", "One-dish meals", 580, 28, 70, 19);
        insertFood(pstmt, "ข้าวขาหมู (1 จาน)", "One-dish meals", 650, 30, 75, 24);
        insertFood(pstmt, "ข้าวมันไก่ (1 จาน)", "One-dish meals", 550, 26, 64, 20);
        insertFood(pstmt, "ก๋วยเตี๋ยวหมู (1 ชาม)", "One-dish meals", 380, 18, 55, 10);
        insertFood(pstmt, "ก๋วยเตี๋ยวเนื้อ (1 ชาม)", "One-dish meals", 420, 22, 56, 12);
        insertFood(pstmt, "ก๋วยเตี๋ยวไก่ (1 ชาม)", "One-dish meals", 350, 20, 52, 8);
        insertFood(pstmt, "ข้าวแกงเขียวหวานไก่ (1 จาน)", "One-dish meals", 480, 22, 58, 18);
        insertFood(pstmt, "ข้าวราดแกงกะหรี่ (1 จาน)", "One-dish meals", 500, 20, 62, 20);
        insertFood(pstmt, "สลัดผัก (1 ชาม)", "One-dish meals", 220, 12, 20, 10);
        insertFood(pstmt, "แซนด์วิชไก่ย่าง", "One-dish meals", 420, 30, 42, 12);

        // Beverages (เครื่องดื่ม)
        insertFood(pstmt, "กาแฟดำ (1 แก้ว)", "Beverages", 2, 0.3, 0, 0);
        insertFood(pstmt, "ชาเขียว (1 แก้ว)", "Beverages", 2, 0, 0, 0);
        insertFood(pstmt, "น้ำส้มคั้น (1 แก้ว)", "Beverages", 112, 2, 26, 0.5);
        insertFood(pstmt, "นมสดจืด (1 แก้ว)", "Beverages", 149, 8, 12, 8);
        insertFood(pstmt, "เชคโปรตีน (1 แก้ว)", "Beverages", 180, 25, 10, 3);
        insertFood(pstmt, "น้ำมะเฟือง (1 แก้ว)", "Beverages", 31, 1, 7, 0.4);
        insertFood(pstmt, "น้ำมะนาว (1 แก้ว)", "Beverages", 28, 0.4, 9, 0.1);
        insertFood(pstmt, "ชามะลิ (1 แก้ว)", "Beverages", 2, 0, 0, 0);
        insertFood(pstmt, "กาแฟสด (1 แก้ว)", "Beverages", 5, 0.3, 0.7, 0.1);
        insertFood(pstmt, "นมถั่วเหลือง (1 แก้ว)", "Beverages", 80, 7, 4, 4);

        pstmt.close();
    }

    /**
     * Helper method to insert food data
     */
    private void insertFood(PreparedStatement pstmt, String name, String category,
            double kcal, double protein, double carbs, double fat) throws SQLException {
        pstmt.setString(1, name);
        pstmt.setString(2, category);
        pstmt.setDouble(3, kcal);
        pstmt.setDouble(4, protein);
        pstmt.setDouble(5, carbs);
        pstmt.setDouble(6, fat);
        pstmt.executeUpdate();
    }

    /**
     * Seed sample exercise data
     */
    private void seedExercises() throws SQLException {
        String sql = "INSERT INTO exercises (name, category, kcal_per_minute) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        // Walking (การเดิน)
        insertExercise(pstmt, "เดินช้า (3 กม./ชม.)", "Walking", 2.5);
        insertExercise(pstmt, "เดินปานกลาง (5 กม./ชม.)", "Walking", 4.0);
        insertExercise(pstmt, "เดินเร็ว (6 กม./ชม.)", "Walking", 5.5);
        insertExercise(pstmt, "เดินขึ้นเขา", "Walking", 6.5);
        insertExercise(pstmt, "เดินบนทางลาดชัน", "Walking", 7.0);

        // Running (การวิ่ง)
        insertExercise(pstmt, "วิ่งจ็อกกิ้ง (8 กม./ชม.)", "Running", 8.0);
        insertExercise(pstmt, "วิ่ง (10 กม./ชม.)", "Running", 10.0);
        insertExercise(pstmt, "วิ่ง (12 กม./ชม.)", "Running", 12.5);
        insertExercise(pstmt, "วิ่งเร็ว (Sprinting)", "Running", 15.0);
        insertExercise(pstmt, "วิ่งระยะไกล", "Running", 9.5);

        // Conditioning Exercise (การบริหารร่างกาย)
        insertExercise(pstmt, "วิดพื้น (Push-ups)", "Conditioning Exercise", 7.0);
        insertExercise(pstmt, "ซิทอัพ (Sit-ups)", "Conditioning Exercise", 5.5);
        insertExercise(pstmt, "เบอร์ปี้ (Burpees)", "Conditioning Exercise", 10.0);
        insertExercise(pstmt, "กระโดดเชือก", "Conditioning Exercise", 11.0);
        insertExercise(pstmt, "แพลงค์ (Plank)", "Conditioning Exercise", 4.0);
        insertExercise(pstmt, "ยกน้ำหนัก", "Conditioning Exercise", 6.0);
        insertExercise(pstmt, "สควอท (Squats)", "Conditioning Exercise", 6.5);
        insertExercise(pstmt, "ลันจ์ (Lunges)", "Conditioning Exercise", 6.0);
        insertExercise(pstmt, "คาร์ดิโอ", "Conditioning Exercise", 8.0);
        insertExercise(pstmt, "โยคะ", "Conditioning Exercise", 3.0);

        // Sports (กีฬา)
        insertExercise(pstmt, "บาสเกตบอล", "Sports", 8.0);
        insertExercise(pstmt, "ฟุตบอล", "Sports", 9.0);
        insertExercise(pstmt, "เทนนิส", "Sports", 7.5);
        insertExercise(pstmt, "ว่ายน้ำ", "Sports", 10.0);
        insertExercise(pstmt, "ปั่นจักรยาน", "Sports", 8.5);
        insertExercise(pstmt, "แบดมินตัน", "Sports", 6.5);
        insertExercise(pstmt, "วอลเลย์บอล", "Sports", 6.0);
        insertExercise(pstmt, "ตีกอล์ฟ", "Sports", 4.5);
        insertExercise(pstmt, "มวยไทย", "Sports", 12.0);
        insertExercise(pstmt, "เทเบิลเทนนิส", "Sports", 4.0);

        // Dancing (การเต้น)
        insertExercise(pstmt, "เต้นบอลรูม", "Dancing", 4.5);
        insertExercise(pstmt, "เต้นฮิปฮอป", "Dancing", 7.0);
        insertExercise(pstmt, "ซุมบ้า (Zumba)", "Dancing", 8.5);
        insertExercise(pstmt, "บัลเล่ต์", "Dancing", 5.5);
        insertExercise(pstmt, "แอโรบิกแดนซ์", "Dancing", 7.5);
        insertExercise(pstmt, "เต้นลีลาศ", "Dancing", 5.0);
        insertExercise(pstmt, "เต้นแจส", "Dancing", 6.5);

        pstmt.close();
    }

    /**
     * Helper method to insert exercise data
     */
    private void insertExercise(PreparedStatement pstmt, String name, String category,
            double kcalPerMinute) throws SQLException {
        pstmt.setString(1, name);
        pstmt.setString(2, category);
        pstmt.setDouble(3, kcalPerMinute);
        pstmt.executeUpdate();
    }

    /**
     * Close database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection!");
            e.printStackTrace();
        }
    }
}
