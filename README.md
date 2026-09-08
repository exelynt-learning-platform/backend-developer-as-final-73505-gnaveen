# Booking API

A secure RESTful Spring Boot application for managing bookings, resources, and reservations with JWT-based authentication and role-based authorization.

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- MySQL
- Spring Security
- JWT (JSON Web Token)
- Bean Validation
- Maven
- Swagger / OpenAPI
- JUnit 5
- MockMvc

## Features

### Booking Management

- Create a booking
- Get all bookings
- Get booking by ID
- Update a booking
- Delete a booking
- Request validation
- Global exception handling

### Resource Management

- Get all resources
- Get resource by ID
- Create resources
- Update resources
- Delete resources
- Role-based access control

### Reservation Management

- Create reservations
- View reservations
- Manage reservation operations
- Protected using JWT authentication

### Authentication & Security

- User login using username and password
- JWT token generation
- JWT token validation
- Stateless authentication
- Role-based authorization
- `USER` and `ADMIN` roles
- Password hashing using BCrypt
- Protected API endpoints
- Public authentication and Swagger endpoints

### API Quality

- Bean Validation
- Centralized exception handling
- Meaningful HTTP status codes
- Swagger/OpenAPI documentation
- Automated security and controller tests

---

# Authentication

Authentication is implemented using JWT.

## Login

### POST `/auth/login`

Example request:

```json
{
  "username": "user",
  "password": "user"
}
```
Successful response:
JSON
{
  "token": "JWT_TOKEN"
}

The returned JWT must be sent with protected requests using:
Authorization: Bearer <JWT_TOKEN>

Default Roles

The application supports:

Role	Access
USER	Read resources and access authenticated reservation endpoints
ADMIN	USER permissions + resource modification

API Endpoints
Authentication
Method	Endpoint	 Description	            Authentication
POST	/auth/login	 Login and receive JWT	  Public

Bookings

| Method | Endpoint             | Description       |
| ------ | -------------------- | ----------------- |
| GET    | `/api/bookings`      | Get all bookings  |
| GET    | `/api/bookings/{id}` | Get booking by ID |
| POST   | `/api/bookings`      | Create a booking  |
| PUT    | `/api/bookings/{id}` | Update a booking  |
| DELETE | `/api/bookings/{id}` | Delete a booking  |

Resources

| Method | Endpoint              | Description        | Required Role |
| ------ | --------------------- | ------------------ | ------------- |
| GET    | `/api/resources`      | Get resources      | USER / ADMIN  |
| GET    | `/api/resources/{id}` | Get resource by ID | USER / ADMIN  |
| POST   | `/api/resources`      | Create resource    | ADMIN         |
| PUT    | `/api/resources/{id}` | Update resource    | ADMIN         |
| DELETE | `/api/resources/{id}` | Delete resource    | ADMIN         |


Example Booking Request
JSON
{
  "customerName": "Test User",
  "email": "test@example.com",
  "eventName": "Spring Boot Conference",
  "bookingDate": "2026-12-20",
  "numberOfSeats": 2
}

Validation

The API validates incoming booking requests.

Examples:

Customer name cannot be blank
Email must be valid
Event name cannot be blank
Booking date must be today or in the future
Number of seats must be at least 1
Error Handling

The application uses centralized exception handling.

Booking Not Found
JSON
{
  "message": "Booking not found with id: 99",
  "status": 404
}

Validation Error
{
  "message": "Validation failed",
  "errors": {
    "numberOfSeats": "Number of seats must be at least 1"
  },
  "status": 400
}

Database Configuration

The application uses MySQL.

Create the database:
SQL>> CREATE DATABASE booking_db;

Database credentials are supplied through environment variables.

The application expects:

DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
JWT_EXPIRATION
## Running the Application

### 1. Configure Environment Variables

Set the following environment variables before starting the application:

```text
DB_URL=jdbc:mysql://localhost:3306/booking_db
DB_USERNAME=root
DB_PASSWORD=your_database_password
JWT_SECRET=your_long_random_jwt_secret
JWT_EXPIRATION=86400000

# Build the Project
mvn clean package

# Run the Application
mvn spring-boot:run

The application runs on:
http://localhost:8081

Swagger / OpenAPI

Swagger UI is available at:

http://localhost:8081/swagger-ui/index.html

OpenAPI specification:

http://localhost:8081/v3/api-docs

Swagger endpoints are publicly accessible.

Testing

The project includes automated tests using JUnit 5 and MockMvc.

Tests cover:

Authentication
JWT authentication
Security authorization
USER role access
ADMIN role access
Unauthorized requests
Forbidden requests
Controller behavior

Run all tests: mvn test

Run a specific test class:

mvn -Dtest=JwtAuthenticationTest test
Security Rules

The application uses stateless JWT authentication.
/auth/**                  → Public
/swagger-ui/**            → Public
/v3/api-docs/**           → Public

GET /api/resources/**     → USER or ADMIN
Other /api/resources/**  → ADMIN

/api/reservations/**      → Authenticated users

Everything else           → Authenticated users
Passwords are stored using BCrypt hashing.

# Project Structure

src/
├── main/
│   ├── java/
│   │   └── com/example/booking/
│   │       ├── config/
│   │       │   └── DataInitializer.java
│   │       │
│   │       ├── controller/
│   │       │   ├── AuthController.java
│   │       │   ├── BookingController.java
│   │       │   ├── ReservationController.java
│   │       │   └── ResourceController.java
│   │       │
│   │       ├── dto/
│   │       │   ├── AuthRequest.java
│   │       │   ├── AuthResponse.java
│   │       │   ├── BookingRequest.java
│   │       │   ├── BookingResponse.java
│   │       │   ├── BookingUpdateRequest.java
│   │       │   ├── ReservationRequest.java
│   │       │   ├── ReservationResponse.java
│   │       │   ├── ResourceRequest.java
│   │       │   └── ResourceResponse.java
│   │       │
│   │       ├── entity/
│   │       │   ├── Booking.java
│   │       │   ├── Reservation.java
│   │       │   ├── ReservationStatus.java
│   │       │   ├── Resource.java
│   │       │   ├── Role.java
│   │       │   └── User.java
│   │       │
│   │       ├── exception/
│   │       │   ├── BookingNotFoundException.java
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   ├── ReservationNotFoundException.java
│   │       │   ├── ResourceNotFoundException.java
│   │       │   └── UserNotFoundException.java
│   │       │
│   │       ├── repository/
│   │       │   ├── BookingRepository.java
│   │       │   ├── ReservationRepository.java
│   │       │   ├── ResourceRepository.java
│   │       │   └── UserRepository.java
│   │       │
│   │       ├── security/
│   │       │   ├── CustomUserDetailsService.java
│   │       │   ├── JwtAuthenticationFilter.java
│   │       │   ├── JwtService.java
│   │       │   └── SecurityConfig.java
│   │       │
│   │       ├── service/
│   │       │   ├── AuthService.java
│   │       │   ├── BookingService.java
│   │       │   ├── ReservationService.java
│   │       │   └── ResourceService.java
│   │       │
│   │       └── BookingApiApplication.java
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    └── java/
        └── com/example/booking/
            ├── controller/
            │   ├── AuthControllerTest.java
            │   └── ReservationControllerTest.java
            │
            └── security/
                ├── SecurityAuthorizationTest.java
                └── JwtAuthenticationTest.java

# HTTP Status Codes

| Status | Meaning                                        |
| ------ | ---------------------------------------------- |
| 200    | Request successful                             |
| 201    | Resource created                               |
| 204    | Resource deleted                               |
| 400    | Validation failed / Bad request                |
| 401    | Authentication required or invalid credentials |
| 403    | Access denied                                  |
| 404    | Resource not found                             |


Security & Credential Protection
Database credentials are provided through environment variables.
JWT secrets are provided through environment variables.
Real credentials are not stored in the repository.
.env files are excluded using .gitignore.
.env.example contains only placeholder values.
Build output and temporary files are excluded from Git.
Passwords are stored using BCrypt hashing.
JWT authentication is stateless.

Git Repository

The repository excludes:

.env
target/
*.log

Author

Developed as a Spring Boot REST API assessment project.