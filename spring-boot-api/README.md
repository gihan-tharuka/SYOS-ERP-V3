# SYOS ERP Spring Boot API

This folder contains the Spring Boot backend migration of the legacy Java Servlet/JSP SYOS ERP project.

The legacy application is still preserved in the repository root as a reference implementation. This module is a new backend API foundation and does not migrate JSP pages or web-store/customer-cart flows.

## Phase 1 Scope

Phase 1 includes:

- Maven Spring Boot project structure.
- Java 17 configuration.
- Spring Web, Spring Data JPA, Validation, MySQL, Flyway, Lombok, and Spring Boot Test dependencies.
- Environment-based MySQL configuration.
- Flyway migrations for the stable ERP tables:
  - `suppliers`
  - `products`
  - `main_stock_batches`
  - `shelf_stock`
  - `reorder_levels`
  - `sales`
  - `bills`
  - `bill_items`
- JPA entities and Spring Data repositories for the same tables.
- A simple health endpoint:
  - `GET /`
  - `GET /api/health`

Phase 1 intentionally does not include product, inventory, sales, or report APIs yet.

## Schema Notes

The legacy `items` table is renamed to `products` for clearer backend terminology.

The legacy `main_stock` table is renamed to `main_stock_batches` because each row represents a supplier batch for a product.

Authentication and web-store/customer checkout tables are not included in Phase 1 because the inspected legacy schema was incomplete for those flows.

## MySQL Configuration

The app reads database settings from environment variables:

```bash
export SYOS_DB_URL='jdbc:mysql://localhost:3306/syos_erp?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
export SYOS_DB_USERNAME='root'
export SYOS_DB_PASSWORD=''
export SYOS_API_PORT='8080'
```

Flyway is enabled by default. To disable it temporarily:

```bash
export SYOS_FLYWAY_ENABLED=false
```

## Run

From this folder:

```bash
mvn test
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080/api/health
```

Expected response:

```json
{"status":"UP"}
```
