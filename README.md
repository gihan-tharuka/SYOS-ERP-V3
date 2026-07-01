# SYOS ERP - Spring Boot & React Migration

## Overview

SYOS ERP started as a Java Servlet/JSP retail ERP system for outlet operations. The original Servlet/JSP application is preserved in this repository as legacy reference code, while the main portfolio implementation modernizes the core workflow into a Spring Boot REST API with a React dashboard.

The modern version focuses on product management, stock movement, reorder visibility, POS sale creation, bill generation, API documentation, Docker-based setup, and automated backend testing.

## Tech Stack

Backend:

- Java 17
- Spring Boot
- Spring Data JPA / Hibernate
- Maven
- Jakarta Bean Validation
- JUnit / Mockito

Frontend:

- React
- Vite
- TypeScript
- Tailwind CSS

Database:

- MySQL
- Flyway database migrations

Tooling:

- Swagger / OpenAPI
- Docker Compose
- GitHub Actions

## Features

Backend:

- Product and supplier management
- Main stock and shelf stock management
- Stock reshelving workflow
- Reorder alerts
- Stock reports
- POS sale creation
- Bill generation and lookup
- DTO-based validation
- Clean JSON error responses

Frontend:

- Dashboard with product, low-stock, and sales summary
- Products CRUD screen
- Inventory and reshelving UI
- Reports page for stock summary and reorder alerts
- POS sale screen
- Sales list and bill lookup

Legacy Servlet/JSP application:

- Admin, cashier, and customer JSP areas
- Servlet-based controllers and DAO-style database access
- Legacy web store/customer checkout code preserved for reference
- Legacy authentication/session flow preserved in the original codebase

## Architecture Summary

The repository now contains two generations of the same ERP idea:

- The legacy Servlet/JSP app remains untouched as the original implementation.
- `spring-boot-api/` contains the modern backend REST API.
- `frontend/` contains the React dashboard.
- The React dashboard communicates with the Spring Boot backend through REST endpoints.
- The Spring Boot backend uses JPA repositories, service-layer business logic, Flyway migrations, and MySQL.

## Folder Structure

```text
SYOS-ERP-V3/
├── spring-boot-api/      # Modern Spring Boot REST API
├── frontend/             # React + Vite + TypeScript dashboard
├── admin/                # Legacy admin JSP pages
├── cashier/              # Legacy cashier JSP pages
├── jsp/                  # Legacy JSP views by module
├── src/                  # Legacy Java Servlet/DAO/model source code
├── WEB-INF/              # Legacy web app configuration and classes
├── resources/            # Legacy SQL/resources/images
├── lib/                  # Legacy libraries
└── .github/workflows/    # CI workflow for the Spring Boot API
```

## Run With Docker

The Docker Compose setup lives in the Spring Boot module.

```bash
cd spring-boot-api
docker compose up --build
```

The API will be available at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

If port `3306` is already used by a local MySQL or MariaDB service, run Compose with a different host MySQL port:

```bash
cd spring-boot-api
MYSQL_PORT=3307 docker compose up --build
```

## Run Locally

### Backend

Prerequisites:

- Java 17+
- Maven
- MySQL-compatible database

From the Spring Boot module:

```bash
cd spring-boot-api
mvn spring-boot:run
```

### Frontend

From the React module:

```bash
cd frontend
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## Environment Variables

Backend:

```text
SYOS_DB_URL=jdbc:mysql://localhost:3306/syos_erp?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
SYOS_DB_USERNAME=root
SYOS_DB_PASSWORD=
SYOS_CORS_ALLOWED_ORIGINS=http://localhost:5173
```

Docker Compose also supports:

```text
MYSQL_DATABASE=syos_erp
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_USER=syos
MYSQL_PASSWORD=syospassword
MYSQL_PORT=3306
SYOS_API_PORT=8080
```

Frontend:

```text
VITE_API_BASE_URL=http://localhost:8080
```

## Testing

Backend tests:

```bash
cd spring-boot-api
mvn test
```

Current backend status: 18 tests passing.

Frontend production build:

```bash
cd frontend
npm run build
```

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Main API groups:

- Suppliers
- Products
- Inventory
- Reorder levels
- Reports
- Sales
- Bills

## Demo Workflow

1. View products.
2. Add stock through the backend API.
3. Reshelve stock from main stock to shelf stock.
4. Check stock and reorder reports.
5. Create a POS sale.
6. View the generated bill.

## Modernization Highlights

- Migrated from Servlet/JSP request handling to a layered Spring Boot REST API.
- Replaced raw JDBC-style data access with Spring Data JPA repositories.
- Added DTOs, validation, and global exception handling.
- Added Flyway migrations for repeatable schema setup.
- Added Docker Compose for MySQL and API startup.
- Added GitHub Actions CI for backend tests.
- Added a React dashboard for the ERP workflow.
- Preserved the legacy Servlet/JSP implementation for comparison and reference.

## Legacy Servlet/JSP Setup Notes

The original app is still available in the repository root and can be reviewed or run separately.

Legacy prerequisites:

- Java Development Kit 17+
- Apache Tomcat 10.x
- MariaDB 10.4.x or MySQL-compatible database

Legacy database setup:

```bash
mysql -u yourusername -p authentication < resources/authentication.sql
```

Legacy deployment:

1. Deploy the root web application to Tomcat.
2. Configure database connection parameters in the legacy connection classes if needed.
3. Start Tomcat.
4. Access the legacy app at a URL similar to:

```text
http://localhost:8080/SYOS-ERP-V3/
```

These notes are for the preserved Servlet/JSP version. The Spring Boot + React implementation is the main portfolio project.

## Known Limitations

- Authentication is not implemented in the Spring Boot version yet.
- Web store/customer checkout from the legacy app is not migrated.
- PDF report generation is not included.
- The frontend is an admin/demo dashboard, not a production ERP UI.
- Supplier, main stock, shelf stock, and reorder-level create/edit workflows are available through the backend API; the current React UI focuses on product CRUD, inventory visibility, reshelving, reports, POS, and bill lookup.

## Portfolio Note

This project demonstrates Java, Spring Boot, REST API design, MySQL, Flyway, JPA/Hibernate, React, TypeScript, Docker, testing, CI, and practical legacy system modernization.

