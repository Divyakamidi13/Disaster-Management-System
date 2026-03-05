# Disaster Management and Alert System - JWT Authentication

## Overview
This is a Spring Boot backend for a Disaster Management and Alert System with JWT Authentication and Role-Based Access Control (RBAC).

## Features
- JWT-based authentication
- Role-based access control (ADMIN, RESPONDER, CLIENT)
- Password hashing with BCrypt
- Secure RESTful APIs
- MySQL database integration

## User Roles
- **ADMIN**: Full access to manage alerts and users
- **RESPONDER**: Can issue and view alerts, cannot manage users
- **CLIENT**: Can only view alerts

## Database Setup
1. Create a MySQL database named `disaster_management`
2. Update `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/disaster_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=your_password_here
   ```

## API Endpoints

### Authentication
- `POST /auth/register` - Register a new user
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "role": "ADMIN"
  }
  ```
- `POST /auth/login` - User login
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- `POST /auth/logout` - User logout

### Disaster Alerts (Protected)
- `GET /api/alerts` - Get all active alerts (All roles)
- `GET /api/alerts/all` - Get all alerts including inactive (ADMIN only)
- `GET /api/alerts/{id}` - Get alert by ID (All roles)
- `POST /api/alerts` - Create new alert (ADMIN, RESPONDER)
- `PUT /api/alerts/{id}` - Update alert (ADMIN only)
- `DELETE /api/alerts/{id}` - Delete alert (ADMIN only)
- `GET /api/alerts/type/{disasterType}` - Get alerts by type (All roles)
- `GET /api/alerts/level/{alertLevel}` - Get alerts by level (All roles)
- `GET /api/alerts/location/{location}` - Get alerts by location (All roles)
- `GET /api/alerts/recent/{hours}` - Get recent alerts (All roles)
- `GET /api/alerts/critical` - Get critical alerts (All roles)

### User Management (Admin only)
- `GET /api/admin/users` - Get all users
- `GET /api/admin/users/{id}` - Get user by ID
- `PUT /api/admin/users/{id}` - Update user
- `DELETE /api/admin/users/{id}` - Delete user

## Running the Application
1. Ensure MySQL is running and the database is created
2. Update database credentials in `application.properties`
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The application will start on `http://localhost:8080`

## Sample Usage

### 1. Register an Admin User
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@disaster.com",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@disaster.com",
    "password": "admin123"
  }'
```

### 3. Create Alert (with JWT token)
```bash
curl -X POST http://localhost:8080/api/alerts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Earthquake Alert",
    "description": "Magnitude 6.5 earthquake detected",
    "disasterType": "EARTHQUAKE",
    "alertLevel": "HIGH",
    "location": "San Francisco, CA",
    "latitude": 37.7749,
    "longitude": -122.4194
  }'
```

## JWT Token Format
The JWT token contains:
- `userId` - User ID
- `email` - User email
- `role` - User role (ADMIN/RESPONDER/CLIENT)
- `iat` - Issued at timestamp
- `exp` - Expiration timestamp (1 hour)

## Security Features
- Passwords are hashed using BCrypt
- JWT tokens are signed with HS256 algorithm
- All protected endpoints require valid JWT token
- Role-based access control enforced at method level
- CORS configuration for cross-origin requests

## Project Structure
```
com.varsha.disastermanagement
├── DisastermanagementApplication.java
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── DisasterAlertController.java
│   └── AdminController.java
├── dto/
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   └── RegisterRequest.java
├── entity/
│   ├── User.java
│   └── DisasterAlert.java
├── enums/
│   ├── Role.java
│   ├── DisasterType.java
│   └── AlertLevel.java
├── repository/
│   ├── UserRepository.java
│   └── DisasterAlertRepository.java
├── security/
│   └── JwtAuthenticationFilter.java
├── service/
│   ├── CustomUserDetailsService.java
│   ├── UserService.java
│   └── DisasterAlertService.java
└── util/
    └── JwtUtil.java
```
