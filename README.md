# Spring Boot Microservices Learning

A hands-on Java and Spring Boot learning project focused on building REST APIs, microservices, database integrations, security, automated testing, and cloud-native applications.

This repository documents my practical learning journey through a structured 30-day Spring Boot and Microservices development programme.

The goal is to strengthen my skills in backend development, microservices architecture, API security, automated testing, containerisation, messaging, CI/CD, and cloud deployment.

## 1. Project Overview

The project currently contains two independent Spring Boot applications:

| Microservice | Port | Description |
|---|---|---|
| Customer Service | 8081 | Manages customer information, CRUD operations, pagination, searching, database persistence and JWT-based security |
| Order Service | 8082 | Manages order creation and retrieval, request validation and exception handling |

Both applications are independently executable Maven projects.

## 2. Technology Stack

**Backend**
- Java 21
- Spring Boot 4.x
- Spring Web MVC
- Spring Data JPA
- Spring Security
- REST APIs
- Maven

**Database**
- PostgreSQL
- Hibernate / JPA
- Flyway database migrations

**Security**
- JWT authentication
- Spring Security
- Role-based authorization (USER and ADMIN)
- BCrypt password encoding

**Testing**
- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- Postman

**Development Tools**
- IntelliJ IDEA
- Git and GitHub
- PowerShell
- pgAdmin

## 3. Repository Structure

```text
spring-boot-microservices-learning/
│
├── customer-service/
│   ├── pom.xml
│   ├── src/
│   └── mvnw.cmd
│
├── order-service/
│   ├── pom.xml
│   ├── src/
│   └── mvnw.cmd
│
├── .gitignore
├── .gitattributes
└── README.md
```

Each microservice has its own application configuration, dependencies, source code and tests.

## 4. Architecture

```text
                    Postman / API Client
                            |
              +-------------+-------------+
              |                           |
              v                           v
      Customer Service              Order Service
         Port 8081                    Port 8082
              |                           |
      Spring Security JWT          REST Controllers
              |                           |
       Service Layer                Service Layer
              |                           |
      Spring Data JPA            In-Memory Storage
              |
         PostgreSQL
```

**Current architecture:** Both services run independently.

Service-to-service REST communication, resilience, asynchronous messaging, and containerisation will be introduced in future learning stages.

## 5. Customer Service

Customer Service provides REST APIs for managing customer information.

### Features

- Create, retrieve, update and delete customers
- Request validation using DTOs
- Global exception handling
- DTO mapping
- PostgreSQL persistence using Spring Data JPA
- Flyway database migrations
- Transaction management
- Pagination and sorting
- Customer searching by name and email domain
- JWT authentication
- Role-based API authorization
- Automated unit and integration testing

### Main API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Authenticate and obtain JWT |
| GET | `/api/customers` | Retrieve customers |
| GET | `/api/customers/{id}` | Retrieve customer by ID |
| POST | `/api/customers` | Create customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |
| GET | `/api/customers/paged` | Paginated customer retrieval |
| GET | `/api/customers/search` | Search by customer name |
| GET | `/api/customers/search/email` | Search by email domain |

### Security

The application uses JWT-based authentication and role-based authorization.

- `USER` role: Read-only access to permitted customer endpoints
- `ADMIN` role: Read and write access to customer endpoints

JWT tokens are supplied through the HTTP `Authorization` header:

```http
Authorization: Bearer <access-token>
```

Credentials and secrets should be provided through environment variables rather than committed to Git.

## 6. Order Service

Order Service is an independent Spring Boot REST application.

### Features

- Create new orders
- Retrieve all orders
- Retrieve an order by ID
- Request validation
- Global exception handling
- Java records for data models and DTOs
- Thread-safe in-memory order storage
- Automated testing

### Main API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Create an order |
| GET | `/api/orders` | Retrieve all orders |
| GET | `/api/orders/{id}` | Retrieve an order by ID |

### Example: Create Order

**Request**

```http
POST http://localhost:8082/api/orders
Content-Type: application/json
```

```json
{
  "customerId": 101,
  "productName": "Laptop",
  "quantity": 2,
  "price": 1500.00
}
```

**Example Response**

```json
{
  "id": 1,
  "customerId": 101,
  "productName": "Laptop",
  "quantity": 2,
  "price": 1500.00
}
```

**Note:** Order Service currently uses in-memory storage. Orders are not persisted across application restarts. Customer IDs are not yet validated against Customer Service.

## 7. Running the Applications Locally

### Prerequisites

- JDK 21
- Maven
- PostgreSQL
- Git
- IntelliJ IDEA or another Java IDE
- Postman (recommended)

### Clone the Repository

```bash
git clone https://github.com/kamran79/spring-boot-microservices-learning.git
cd spring-boot-microservices-learning
```

### Start Customer Service

Ensure PostgreSQL is running and the required customer database is available.

Configure the environment variables in PowerShell:

```powershell
$env:DB_PASSWORD = "<your-local-database-password>"

$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
$rng.Dispose()
```

Start the application:

```powershell
cd customer-service
mvn spring-boot:run
```

Customer Service:

`http://localhost:8081`

### Start Order Service

Open another terminal from the repository root:

```powershell
cd order-service
mvn spring-boot:run
```

Order Service:

`http://localhost:8082`

Both services can run simultaneously.

## 8. Running Automated Tests

### Customer Service

From the repository root:

```powershell
mvn -f .\customer-service\pom.xml clean test
```

The customer-service test suite includes unit tests, controller/security tests, repository tests and integration tests.

### Order Service

```powershell
mvn -f .\order-service\pom.xml clean test
```

The order-service test suite validates the application according to its currently implemented tests.

**Latest verified learning milestone:** Customer Service — 34 tests passed. Both Maven builds completed successfully.

## 9. Learning Progress

| Day | Topic | Status |
|---|---|---|
| 1 | Spring Boot project setup and first REST API | Completed |
| 2 | REST API testing and application configuration | Completed |
| 3 | CRUD operations and JUnit testing | Completed |
| 4 | DTOs, validation and exception handling | Completed |
| 5 | Request validation and global exception handling | Completed |
| 6 | DTO mapping using MapStruct | Completed |
| 7 | Spring Data JPA and PostgreSQL integration | Completed |
| 8 | Flyway migrations and transaction management | Completed |
| 9 | Pagination, sorting and searching | Completed |
| 10 | Spring Security, JWT and role-based authorization | Completed |
| 11 | Mockito, unit testing and integration testing | Completed |
| 12 | Microservices architecture and Order Service | Completed |
| 13 | REST communication between microservices | Planned |

## 10. Upcoming Learning Topics

The remaining programme will progressively introduce:

- Inter-service REST communication using Spring RestClient
- Authentication between services
- API Gateway
- Service discovery and configuration management
- Resilience, timeouts and retry patterns
- Docker and Docker Compose
- Apache Kafka and asynchronous messaging
- Kubernetes fundamentals and deployment
- Cloud deployment using Google Cloud Platform (GCP)
- CI/CD pipelines
- Observability, logging and monitoring
- Microservices testing and architecture best practices

The implementation will evolve incrementally as these topics are completed.

## 11. Learning Objectives

This repository is designed to develop practical skills in:

1. Designing maintainable RESTful APIs.
2. Implementing secure Spring Boot backend applications.
3. Building independently deployable microservices.
4. Applying database migrations and transaction management.
5. Writing reliable unit and integration tests.
6. Understanding distributed-system communication and resilience.
7. Deploying containerised applications using modern cloud-native tooling.
8. Preparing for senior Java backend and microservices engineering interviews.

## 12. Project Status

**Current milestone:** Day 12 completed.

**Next milestone:** Day 13 — Implement REST communication between Order Service and Customer Service, including JWT authentication, timeouts, error handling and automated testing.

---

This is an ongoing hands-on learning project. Features, architecture and documentation will be updated as the 30-day programme progresses.
