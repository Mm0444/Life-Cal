package com.lifecal.util;

import com.lifecal.common.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseMigration - Utility to run database migrations
 */
public class DatabaseMigration {

    /**
     * Run migration to add email column if it doesn't exist
     */
    public static void migrateAddEmailColumn() {
        Connection connection = DatabaseManager.getInstance().getConnection();

        try {
            // Check if email column already exists
            if (!columnExists(connection, "users", "email")) {
                System.out.println("Running migration: Adding email column to users table...");

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN email TEXT");
                stmt.close();

                System.out.println("Migration completed successfully!");
            } else {
                System.out.println("Email column already exists. Skipping migration.");
            }
        } catch (SQLException e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if a column exists in a table
     */
    private static boolean columnExists(Connection connection, String tableName, String columnName) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ")");

            while (rs.next()) {
                String colName = rs.getString("name");
                if (colName.equalsIgnoreCase(columnName)) {
                    rs.close();
                    stmt.close();
                    return true;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Main method to run migrations
     */
    public static void main(String[] args) {
        System.out.println("=== Database Migration Tool ===");
        migrateAddEmailColumn();
        System.out.println("=== All migrations completed ===");
    }
}
