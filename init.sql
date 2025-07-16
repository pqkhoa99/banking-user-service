-- Database initialization script for user service
-- This file will be executed when the PostgreSQL container starts for the first time

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    kyc_status VARCHAR(50) CHECK (kyc_status IN ('VERIFIED', 'PENDING', 'UNKNOWN')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_kyc_status ON users(kyc_status);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Insert some sample data (optional)
INSERT INTO users (id, email, password_hash, first_name, last_name, kyc_status, created_at) 
VALUES 
    ('550e8400-e29b-41d4-a716-446655440001', 'john.doe@example.com', '$2a$10$example.hash.here', 'John', 'Doe', 'VERIFIED', NOW()),
    ('550e8400-e29b-41d4-a716-446655440002', 'jane.smith@example.com', '$2a$10$example.hash.here', 'Jane', 'Smith', 'PENDING', NOW())
ON CONFLICT (id) DO NOTHING;
 