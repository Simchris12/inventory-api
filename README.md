# Inventory Management REST API

A RESTful API built with **Java 21** and **Spring Boot 3** for managing product inventory. This project implements a clean layered architecture, declarative input validation, custom business rules, global exception handling, and interactive OpenAPI/Swagger documentation.

---

## Tech Stack

* **Java 21** — Core programming language
* **Spring Boot 3** — Backend application framework
* **Spring Web** — RESTful endpoints
* **Spring Data JPA** — Data persistence and repository abstraction
* **PostgreSQL** — Relational database (`inventory_db`)
* **Jakarta Bean Validation** — Request payload validation (`@Valid`)
* **Springdoc OpenAPI (Swagger UI)** — Interactive API documentation
* **Maven** — Dependency management and build tool

---

## Architecture & Key Features

The application follows a strict separation of concerns under the `com.cristofer.inventory` package:

```text
Client / HTTP Request
        |
        v
GlobalExceptionHandler (@RestControllerAdvice) -> Standardized JSON errors (400, 404, 409)
        |
        v
ProductController (@RestController)            -> REST endpoints & validation
        |
        v
ProductService / ProductServiceImpl (@Service) -> Business rules (e.g., unique product names)
        |
        +---> ProductMapper (@Component)       -> Entity <-> DTO conversion
        |
        +---> ProductRepository (JpaRepository)-> Database operations & Query Methods
        |
        v
PostgreSQL (inventory_db)
```

* **Full CRUD Operations:** Create, read, update, and delete products with proper HTTP status codes (`200 OK`, `201 Created`, `204 No Content`).
* **Business Rule Enforcement:** Prevents duplicate product names on both creation and update operations using Spring Data JPA query methods (`existsByName`).
* **Centralized Error Handling:** Intercepts domain and validation exceptions via `@RestControllerAdvice` to return clean, predictable JSON responses (`400 Bad Request`, `404 Not Found`, `409 Conflict`).

---

## API Endpoints

| Method | Endpoint | Description | Success Status | Error Codes |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/products` | Create a new product | `201 Created` | `400`, `409` |
| `GET` | `/products` | Retrieve all products | `200 OK` | — |
| `GET` | `/products/{id}` | Retrieve a product by ID | `200 OK` | `404` |
| `PUT` | `/products/{id}` | Update an existing product | `200 OK` | `400`, `404`, `409` |
| `DELETE` | `/products/{id}` | Delete a product by ID | `204 No Content` | `404` |

### Sample Request (`POST /products`)

```json
{
  "name": "Mechanical Keyboard",
  "price": 89.99,
  "stock": 25
}
```

### Sample Error Response (`409 Conflict`)

```json
{
  "message": "Product already exists with name: Mechanical Keyboard"
}
```

---

## Getting Started

### Prerequisites
* **JDK 21** installed
* **Maven** installed (or use the included Maven wrapper)
* **PostgreSQL** running locally with a database named `inventory_db`

### Installation & Execution

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Simchris12/inventory-api.git
   cd inventory
   ```

2. **Configure the database:**
   Update your PostgreSQL credentials in `src/main/resources/application.properties` (or `application.yml`).

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Explore the Interactive API Documentation (Swagger UI):**
   Once the server is running, open your browser and navigate to:
   ```text
   http://localhost:8080/swagger-ui/index.html
   ```
