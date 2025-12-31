-- Migration: Add email column to users table
-- Date: 2025-11-27
-- Description: Add optional email field for password recovery feature

-- Add email column (nullable)
ALTER TABLE users ADD COLUMN email TEXT;
