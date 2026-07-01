# SYOS ERP Spring Boot API

Spring Boot backend migration of the legacy Java Servlet/JSP SYOS ERP project.

The original Servlet/JSP application is preserved in the repository root as legacy reference code. This module is the modern backend API built for a portfolio MVP: product catalog, supplier management, inventory, reshelving, reorder alerts, stock reports, POS sales, and bill generation.

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Web
- Spring Data JPA / Hibernate
- Jakarta Bean Validation
- MySQL 8
- Flyway
- Lombok
- springdoc-openapi / Swagger UI
- JUnit 5 and Mockito
- Docker Compose
- GitHub Actions CI

## Architecture

The API follows a simple layered structure:

```text
Controller -> DTO -> Service -> Repository -> JPA Entity -> MySQL
```

Controllers stay thin and only handle HTTP routing. Services contain business rules such as stock validation, reshelving, reorder calculations, and transactional POS sale creation. Repositories use Spring Data JPA. Flyway owns schema creation and seed data.

## What Was Modernized

- Replaced Servlet/JSP request handling with Spring MVC REST controllers.
- Replaced hand-written JDBC DAOs with Spring Data JPA repositories.
- Replaced hardcoded DB configuration with environment variables.
- Added Flyway migrations for repeatable database setup.
- Added DTOs and validation instead of exposing persistence classes directly.
- Added clean JSON error responses for validation and missing resources.
- Added transactional inventory movement and POS sale generation.
- Added Swagger UI, Docker Compose, and CI for easier review.

## Current Scope

Implemented:

- Suppliers CRUD
- Products CRUD
- Main stock CRUD
- Shelf stock CRUD
- Reorder level CRUD
- Reshelving from main stock to shelf stock
- Reorder alerts
- Stock summary report
- POS sale creation from shelf stock
- Bill lookup

Not implemented yet:

- Authentication and authorization
- Customer/web store/cart/checkout flows
- Frontend migration
- Image upload handling
- PDF reports

## Local Setup

Prerequisites:

- Java 17
- Maven 3.9+
- MySQL 8 or compatible MySQL/MariaDB server

Create a database and configure environment variables:

```bash
export SYOS_DB_URL='jdbc:mysql://localhost:3306/syos_erp?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
export SYOS_DB_USERNAME='root'
export SYOS_DB_PASSWORD=''
export SYOS_API_PORT='8080'
```

Run tests and start the app:

```bash
mvn test
mvn spring-boot:run
```

Health check:

```text
GET http://localhost:8080/api/health
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Docker Compose Setup

Copy the example env file if you want local overrides:

```bash
cp .env.example .env
```

Start MySQL and the API:

```bash
docker compose up --build
```

The API will be available at:

```text
http://localhost:8080
```

MySQL data is persisted in the `syos_mysql_data` Docker volume.

Validate Compose configuration:

```bash
docker compose config
```

## API Route Summary

Suppliers:

```text
GET    /api/suppliers
GET    /api/suppliers/{id}
POST   /api/suppliers
PUT    /api/suppliers/{id}
DELETE /api/suppliers/{id}
```

Products:

```text
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

Inventory:

```text
GET    /api/inventory/main-stock
GET    /api/inventory/main-stock/{id}
POST   /api/inventory/main-stock
PUT    /api/inventory/main-stock/{id}
DELETE /api/inventory/main-stock/{id}

GET    /api/inventory/shelf-stock
GET    /api/inventory/shelf-stock/{id}
POST   /api/inventory/shelf-stock
PUT    /api/inventory/shelf-stock/{id}
DELETE /api/inventory/shelf-stock/{id}

POST   /api/inventory/shelf-stock/reshelve
```

Reorder levels and reports:

```text
GET    /api/reorder-levels
GET    /api/reorder-levels/{id}
POST   /api/reorder-levels
PUT    /api/reorder-levels/{id}
DELETE /api/reorder-levels/{id}
GET    /api/reorder-levels/alerts

GET    /api/reports/reorder
GET    /api/reports/stock
```

Sales and bills:

```text
POST   /api/sales
GET    /api/sales
GET    /api/sales/{id}
GET    /api/bills/{serialNumber}
```

## Sample API Flow

1. Create a supplier.
2. Create a product.
3. Add a main stock batch for the product.
4. Create shelf stock for the product.
5. Reshelve stock from the main stock batch into shelf stock.
6. Create a POS sale that consumes shelf stock.
7. Retrieve the generated bill.

Create product:

```json
{
  "productCode": "RICE005",
  "productName": "Red rice",
  "price": 180.00,
  "discount": 0.00,
  "imagePath": null
}
```

Create main stock batch:

```json
{
  "productId": 12,
  "supplierId": 1,
  "batchCode": "RICE-BATCH-002",
  "purchaseDate": "2026-07-01",
  "purchasePrice": 140.00,
  "quantity": 100,
  "expiryDate": "2027-01-01"
}
```

Reshelve stock:

```json
{
  "mainStockBatchId": 1,
  "shelfStockId": 1,
  "quantity": 10
}
```

Create POS sale:

```json
{
  "cashierName": "Demo Cashier",
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

Sample bill response:

```json
{
  "saleId": 10,
  "cashierName": "Demo Cashier",
  "saleDateTime": "2026-07-01T10:30:00",
  "billSerialNumber": 15,
  "totalAmount": 3800.00,
  "items": [
    {
      "id": 31,
      "productId": 1,
      "productCode": "ITEM001",
      "productName": "Laptop",
      "quantity": 2,
      "unitPrice": 1500.00,
      "lineTotal": 3000.00
    }
  ]
}
```

## Testing

Run:

```bash
mvn test
```

GitHub Actions also runs this command for changes under `spring-boot-api`.

## Database Notes

Flyway migrations create the MVP schema and seed demo data:

- `suppliers`
- `products`
- `main_stock_batches`
- `shelf_stock`
- `reorder_levels`
- `sales`
- `bills`
- `bill_items`

Legacy names were cleaned up where useful. For example, the old `items` concept is now `products`, and old `main_stock` rows are modeled as `main_stock_batches`.

## Known Limitations

- No authentication or role-based security yet.
- POS sale creation assumes cash payment and sets cash tendered equal to the total.
- Shelf stock consumption uses shelf stock rows in ID order.
- Customer-facing web store and checkout are intentionally excluded because the legacy schema for those flows was incomplete.
- No frontend is included; this module is API-only.
- No PDF/export report generation yet.
