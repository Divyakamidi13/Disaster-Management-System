-- Insert default users for testing
INSERT INTO user (name, email, password, role, region) VALUES 
('Admin User', 'admin@disaster.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', 'Chennai'),
('Responder User', 'responder@disaster.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'AUTHORITY', 'Coimbatore'),
('Client User', 'client@disaster.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'CITIZEN', 'Madurai');

-- Note: The password hash is for "password" (all users have password: password)
