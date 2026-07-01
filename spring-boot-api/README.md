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

## Phase 2 Scope

Phase 2 adds CRUD APIs for the portfolio MVP foundation:

- Suppliers
- Products
- Main stock batches
- Shelf stock
- Reorder levels

Sales, billing, report, authentication, web-store, customer-cart, checkout, and image-upload APIs are planned for later phases.

## Phase 2 API Routes

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

Main stock:

```text
GET    /api/inventory/main-stock
GET    /api/inventory/main-stock/{id}
POST   /api/inventory/main-stock
PUT    /api/inventory/main-stock/{id}
DELETE /api/inventory/main-stock/{id}
```

Shelf stock:

```text
GET    /api/inventory/shelf-stock
GET    /api/inventory/shelf-stock/{id}
POST   /api/inventory/shelf-stock
PUT    /api/inventory/shelf-stock/{id}
DELETE /api/inventory/shelf-stock/{id}
```

Reorder levels:

```text
GET    /api/reorder-levels
GET    /api/reorder-levels/{id}
POST   /api/reorder-levels
PUT    /api/reorder-levels/{id}
DELETE /api/reorder-levels/{id}
```

## Sample Requests

Create supplier:

```json
{
  "username": "supplier3",
  "companyName": "Fresh Goods Ltd.",
  "contactPerson": "Amara Silva",
  "email": "amara@freshgoods.example",
  "mobile": "0771234567"
}
```

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

Create shelf stock:

```json
{
  "productId": 12,
  "shelfCapacity": 50,
  "currentQuantity": 20
}
```

Create reorder level:

```json
{
  "productId": 12,
  "thresholdQuantity": 50,
  "totalStock": 100
}
```

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
