-- Create disaster_events table for Advanced Disaster Monitoring Module
CREATE TABLE IF NOT EXISTS disaster_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    disaster_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    location_name VARCHAR(255),
    source VARCHAR(100) NOT NULL,
    event_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    approved_by VARCHAR(255),
    approved_at DATETIME,
    auto_approved BOOLEAN DEFAULT FALSE,
    
    INDEX idx_disaster_type (disaster_type),
    INDEX idx_severity (severity),
    INDEX idx_status (status),
    INDEX idx_event_time (event_time),
    INDEX idx_location_name (location_name),
    INDEX idx_created_at (created_at)
);
