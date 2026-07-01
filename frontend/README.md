# SYOS ERP React Dashboard

Portfolio frontend for the Spring Boot migration of the legacy SYOS Servlet/JSP ERP. It focuses on the core API workflow already implemented in `spring-boot-api`: products, inventory movement, reports, POS sales, and bill lookup.

## Tech Stack

- React 19
- Vite
- TypeScript
- Tailwind CSS 4
- lucide-react icons

## Prerequisites

- Node.js 20.19+ or 22.12+
- The Spring Boot API running on `http://localhost:8080`
- MySQL configured for the backend

## Environment

Create a local env file:

```bash
cp .env.example .env
```

Default value:

```text
VITE_API_BASE_URL=http://localhost:8080
```

## Run The Backend

From the Spring Boot module:

```bash
cd spring-boot-api
mvn spring-boot:run
```

Swagger is available at:

```text
http://localhost:8080/swagger-ui.html
```

## Run The Frontend

From this folder:

```bash
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

## Build

```bash
npm run build
```

## Demo Flow

1. Open the dashboard to confirm API connectivity and counts.
2. Create or edit products in Products.
3. Review main stock and shelf stock in Inventory.
4. Use the reshelving form to move stock from a main stock batch to shelf stock.
5. Check stock summary and reorder alerts in Reports.
6. Create a POS sale and review the generated bill.
7. Look up bills by serial number in Sales/Bills.

## Current Limitations

- No authentication or role-based access yet.
- Supplier, main stock, shelf stock, and reorder-level create/edit screens remain backend-only for now.
- No web store, customer cart, image upload, frontend legacy JSP migration, or PDF reports.
