# Order Management System

A robust, enterprise-grade Order Management System built with Spring Boot, implementing Clean Architecture principles with a focus on maintainability, testability, and scalability.

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Architecture & Design Decisions](#architecture--design-decisions)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Security](#security)
- [Testing](#testing)
- [Future Scope](#future-scope)

## Overview

This Order Management System is a full-featured application that handles product catalog management, user authentication, and order processing with intelligent discount strategies. The system is designed with enterprise best practices, including JWT-based authentication, role-based access control, caching, and comprehensive error handling.

## Key Features

### 🛍️ Product Management
- CRUD operations for products (Admin only)
- Public product listing and search
- Advanced filtering (by name, price range, stock availability)
- Redis-style caching for improved performance
- Soft delete functionality

### 👤 User Authentication & Authorization
- JWT-based authentication
- Role-based access control (ADMIN, USER, PREMIUM_USER)
- Secure password encryption with BCrypt
- Token expiration handling

### 📦 Order Management
- Place orders with multiple items
- Real-time inventory validation
- Automatic discount calculation using Strategy Pattern
- Transaction management for data consistency
- Order history tracking

### 💰 Intelligent Discount System
- **Premium User Discount**: 10% off for PREMIUM_USER role
- **High-Value Order Discount**: 5% off for orders over 500
- Extensible discount strategy pattern
- Best discount automatically applied

## Tech Stack

### Core Framework
- **Java 17** - Programming Language
- **Spring Boot 3.4.12** - Application Framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data Access Layer
- **Hibernate** - ORM Framework

### Database
- **H2 Database** - In-memory database (Development)
- **Flyway** - Database Migration Tool

### Security
- **JWT (JSON Web Tokens)** - Stateless Authentication
- **BCrypt** - Password Hashing

### API Documentation
- **SpringDoc OpenAPI** - API Documentation (Swagger)

### Build & DevOps
- **Maven** - Build Tool
- **Lombok** - Boilerplate Code Reduction
- **MapStruct** - Object Mapping

### Additional Libraries
- **Jackson** - JSON Processing
- **Bean Validation** - Request Validation

## Architecture & Design Decisions

### 1. Clean Architecture (Hexagonal Architecture)

The application follows Clean Architecture principles with clear separation of concerns:

```
com.assignment.ordermanagement/
├── [module]/                    # Each module (order, product, user)
│   ├── adapter/                 # Adapter Layer (Controllers)
│   │   └── rest/                # REST API Controllers
│   ├── application/             # Application Layer
│   │   ├── dto/                 # Data Transfer Objects
│   │   └── usecase/             # Use Case Implementations
│   ├── domain/                  # Domain Layer (Core Business Logic)
│   │   ├── model/               # Domain Models
│   │   ├── port/                # Ports (Interfaces)
│   │   ├── service/             # Domain Services
│   │   └── exception/           # Domain Exceptions
│   ├── infrastructure/          # Infrastructure Layer
│   │   ├── persistence/         # Database Implementation
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── repository/      # JPA Repositories
│   │   │   ├── mapper/          # Entity-Domain Mappers
│   │   │   └── adapter/         # Repository Adapters
│   │   ├── security/            # Security Implementation
│   │   └── cache/               # Caching Implementation
│   └── config/                  # Module Configuration
└── shared/                      # Shared Components
    ├── config/                  # Global Configuration
    └── exception/               # Global Exception Handling
```

**Benefits:**
- **Independence**: Business logic is independent of frameworks and UI
- **Testability**: Easy to test each layer in isolation
- **Flexibility**: Easy to swap implementations (e.g., database, caching)
- **Maintainability**: Clear boundaries make code easier to understand and modify

### 2. Strategy Pattern for Discount Calculation

Implemented Strategy Pattern for flexible discount rules:

```java
public interface DiscountStrategy {
    BigDecimal calculate(OrderContext context);
}
```

**Strategies:**
- `PremiumUserDiscount`: 10% discount for premium users
- `HighValueOrderDiscount`: 5% discount for orders over 500

**Benefits:**
- Easy to add new discount strategies without modifying existing code (Open/Closed Principle)
- Business rules are encapsulated and testable
- Runtime strategy selection based on order context

### 3. Repository Pattern with Ports and Adapters

Domain layer defines ports (interfaces), infrastructure provides adapters:

```
Domain Layer (Port)          Infrastructure Layer (Adapter)
    ↓                               ↓
OrderRepository  ←────────  OrderRepositoryAdapter
                            (implements OrderRepository)
                                    ↓
                            OrderRepositoryJpa
                            (Spring Data JPA)
```

**Benefits:**
- Domain logic doesn't depend on persistence implementation
- Easy to switch databases or add caching layers
- Improved testability with mock implementations

### 4. Use Case Driven Design

Each business operation is a distinct use case:

- `PlaceOrderUseCase`
- `CreateProductUseCase`
- `AuthenticateUserUseCase`

**Benefits:**
- Single Responsibility Principle
- Clear business operations
- Easy to understand and test

### 5. DTO Pattern

Separate DTOs for API layer to decouple internal domain models from external API:

**Benefits:**
- API stability: Internal changes don't affect API contracts
- Validation at API boundary
- Security: Don't expose internal model structure

### 6. Global Exception Handling

Centralized exception handling with `@RestControllerAdvice`:

**Benefits:**
- Consistent error responses
- Clean controller code
- Better error logging and monitoring

### 7. Caching Strategy

Product repository wrapped with caching decorator:

```
CachingProductRepositoryAdapter → ProductRepositoryAdapter → JPA Repository
```

**Benefits:**
- Improved read performance
- Easy to enable/disable caching
- Transparent to business logic

## Prerequisites

Before running the application, ensure you have:

- **JDK 17** or higher
- **Maven 3.8+** (or use included Maven wrapper)
- **Git** (for cloning the repository)
- **Postman** or similar tool (for API testing)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd shop
```

### 2. Build the Application

Using Maven Wrapper (recommended):

```bash
# Windows
.\mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

Or using Maven directly:

```bash
mvn clean install
```

### 3. Run the Application

Using Maven Wrapper:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

Or using Maven:

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/ordermanagement-0.0.1-SNAPSHOT.jar
```

### 4. Verify Application is Running

The application will start on `http://localhost:8080`

Check the health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

### 5. Access API Documentation

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

### 6. Access H2 Database Console (Optional)

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave empty)
```

## API Documentation

### Base URL

```
http://localhost:8080/api
```

### Default Users

The application comes with pre-configured users for testing:

| Username | Password  | Role          | Description                    |
|----------|-----------|---------------|--------------------------------|
| admin    | password  | ADMIN         | Admin user (manage products)   |
| user     | password  | USER          | Regular user (place orders)    |
| rishi    | password  | PREMIUM_USER  | Premium user (10% discount)    |

### Authentication

#### 1. Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ADMIN"
}
```

**Use the token in subsequent requests:**
```http
Authorization: Bearer <token>
```

### Product APIs

#### 1. Get All Products (Public)

```http
GET /api/products
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 50
  }
]
```

#### 2. Search Products (Public)

```http
GET /api/products/search?name=Laptop&minPrice=500&maxPrice=2000&inStock=true
```

**Query Parameters:**
- `name` (optional): Product name (case-insensitive, partial match)
- `minPrice` (optional): Minimum price
- `maxPrice` (optional): Maximum price
- `inStock` (optional): Filter by stock availability (true/false)

#### 3. Create Product (Admin Only)

```http
POST /api/products
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 29.99,
  "quantity": 100
}
```

**Response:**
```json
{
  "id": 4,
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 29.99,
  "quantity": 100
}
```

#### 4. Update Product (Admin Only)

```http
PUT /api/products/{id}
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Updated Product Name",
  "description": "Updated description",
  "price": 39.99,
  "quantity": 150
}
```

#### 5. Delete Product (Admin Only)

```http
DELETE /api/products/{id}
Authorization: Bearer <admin-token>
```

**Response:** `204 No Content`

### Order APIs

#### 1. Place Order (Authenticated Users)

```http
POST /api/orders
Authorization: Bearer <user-token>
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**Response:**
```json
{
  "id": 1,
  "userId": 2,
  "orderTotal": 2429.97,
  "createdAt": "2025-12-19T10:30:00",
  "items": [
    {
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "unitPrice": 999.99,
      "discountApplied": 199.99,
      "totalPrice": 1799.99
    },
    {
      "productId": 2,
      "productName": "Keyboard",
      "quantity": 1,
      "unitPrice": 79.99,
      "discountApplied": 0.00,
      "totalPrice": 79.99
    }
  ]
}
```

**Discount Rules:**
- If user has PREMIUM_USER role: 10% discount on entire order
- If order subtotal >= 500: 5% discount on entire order
- Only the best discount is applied (not cumulative)

### Error Responses

All error responses follow a consistent format:

**400 Bad Request (Validation Error):**
```json
{
  "message": "Validation failed",
  "errors": {
    "name": "must not be blank",
    "price": "must be greater than 0"
  }
}
```

**404 Not Found:**
```json
{
  "message": "Product not found with id: 99"
}
```

**401 Unauthorized:**
```json
{
  "message": "Invalid credentials"
}
```

**403 Forbidden:**
```json
{
  "message": "Access Denied"
}
```

**400 Bad Request (Business Logic):**
```json
{
  "message": "Product 'Laptop' is out of stock. Requested: 10, Available: 5"
}
```

## Database Schema

### Users Table

| Column   | Type         | Constraints      |
|----------|--------------|------------------|
| id       | BIGINT       | PRIMARY KEY      |
| username | VARCHAR(50)  | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL         |
| role     | VARCHAR(20)  | NOT NULL         |

### Products Table

| Column      | Type          | Constraints       |
|-------------|---------------|-------------------|
| id          | BIGINT        | PRIMARY KEY       |
| name        | VARCHAR(100)  | NOT NULL          |
| description | VARCHAR(255)  |                   |
| price       | DECIMAL(10,2) | NOT NULL          |
| quantity    | INT           | NOT NULL          |
| deleted     | BOOLEAN       | DEFAULT FALSE     |
| created_at  | TIMESTAMP     | DEFAULT CURRENT   |
| updated_at  | TIMESTAMP     | DEFAULT CURRENT   |

### Orders Table

| Column      | Type          | Constraints       |
|-------------|---------------|-------------------|
| id          | BIGINT        | PRIMARY KEY       |
| user_id     | BIGINT        | FOREIGN KEY, NOT NULL |
| order_total | DECIMAL(10,2) | NOT NULL          |
| created_at  | TIMESTAMP     | DEFAULT CURRENT   |

### Order Items Table

| Column           | Type          | Constraints       |
|------------------|---------------|-------------------|
| id               | BIGINT        | PRIMARY KEY       |
| order_id         | BIGINT        | FOREIGN KEY, NOT NULL |
| product_id       | BIGINT        | NOT NULL          |
| quantity         | INT           | NOT NULL          |
| unit_price       | DECIMAL(10,2) | NOT NULL          |
| discount_applied | DECIMAL(10,2) |                   |
| total_price      | DECIMAL(10,2) | NOT NULL          |

## Security

### Authentication Flow

1. User sends credentials to `/api/auth/login`
2. Server validates credentials
3. Server generates JWT token with user details and role
4. Client stores token and includes it in subsequent requests
5. `JwtAuthenticationFilter` validates token on each request
6. `SecurityContext` is populated with user details

### Authorization

Access control is enforced at the controller level using `@PreAuthorize`:

- **Public Access**: Product listing, search
- **USER/PREMIUM_USER**: Place orders
- **ADMIN**: Create, update, delete products

### Password Security

- Passwords are hashed using BCrypt with salt
- Never stored or transmitted in plain text
- Strong hashing algorithm (cost factor: 10)

### Token Configuration

- **Algorithm**: HMAC SHA-256
- **Expiration**: 24 hours (configurable)
- **Secret Key**: Configured in `application.yaml` (should be environment-specific in production)

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

### Testing with Postman

1. Import the API endpoints from Swagger UI
2. Create an environment with:
   - `baseUrl`: `http://localhost:8080/api`
   - `token`: (set after login)
3. Login and save the token
4. Use the token in subsequent requests

### Manual Testing Flow

1. **Login as admin:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"password"}'
   ```

2. **Create a product:**
   ```bash
   curl -X POST http://localhost:8080/api/products \
     -H "Authorization: Bearer <admin-token>" \
     -H "Content-Type: application/json" \
     -d '{"name":"Test Product","description":"Test","price":50,"quantity":100}'
   ```

3. **Login as user:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"user","password":"password"}'
   ```

4. **Place an order:**
   ```bash
   curl -X POST http://localhost:8080/api/orders \
     -H "Authorization: Bearer <user-token>" \
     -H "Content-Type: application/json" \
     -d '{"items":[{"productId":1,"quantity":2}]}'
   ```

## Configuration

### Application Configuration

Key configuration properties in `src/main/resources/application.yaml`:

```yaml
jwt:
  secret: <your-secret-key>  # Change in production
  expiration: 86400000       # 24 hours

spring:
  datasource:
    url: jdbc:h2:mem:testdb  # Use PostgreSQL/MySQL in production
```

### Environment-Specific Configuration

For production deployment, create `application-prod.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ordermanagement
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

jwt:
  secret: ${JWT_SECRET}
  expiration: 3600000  # 1 hour for production

logging:
  level:
    root: INFO
```

Run with production profile:
```bash
java -jar app.jar --spring.profiles.active=prod
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


---

**Built with ❤️ using Spring Boot and Clean Architecture**

