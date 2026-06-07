CREATE DATABASE IF NOT EXISTS numberbook
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE numberbook;

CREATE TABLE IF NOT EXISTS contact (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    source VARCHAR(50) DEFAULT 'mobile',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_phone (phone)
);