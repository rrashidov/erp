# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Configure `.envrc` with `export mvn_home=<path-to-maven>/bin/mvn` before running any make targets.

```bash
# Build all modules
make build

# Run unit tests for a specific module
./mvnw -pl erp.backend test
./mvnw -pl erp.frontend test

# Run a single test class
./mvnw -pl erp.backend test -Dtest=CustomerControllerTest

# Full local stack (builds images, starts docker-compose)
make start-locally

# Stop local stack
make stop-locally

# Run integration tests (requires local stack running)
make run-integration-tests

# Seed test data via REST API
make init-test-data
```

## Architecture

Three Maven modules plus an integration test module:

- **`erp.dto`** — shared DTOs used by both frontend and backend for API communication (e.g. `SalesDocumentDTO`, `ItemDTO`). No Spring dependency; pure POJOs.
- **`erp.backend`** — Spring Boot REST API on port `8082`. Owns JPA entities, repositories, services, and controllers. Connects to MySQL (H2 in-memory by default). Sends RabbitMQ messages to trigger async post operations.
- **`erp.frontend`** — Spring Boot web app on port `8081`. Uses Thymeleaf templates. Controllers call backend REST API via service classes (no direct DB access). Routes all user actions through the backend.
- **`itests`** — standalone integration tests that exercise the backend REST API end-to-end against a running stack.

### Backend internal layers

```
controllers/  →  services/  →  repositories/  →  JPA entities (model/)
                     ↓
               beans/rabbitmq/   (async post receivers, listen on RabbitMQ queues)
```

Each domain object (Item, Customer, Vendor, SalesOrder, etc.) follows the same pattern:
- `XxxRepository` — Spring Data JPA
- `XxxService` / `XxxServiceImpl` — business logic
- `XxxController` — REST endpoints under `/api/v1/`

### Async posting flow

Posting a document (Sales Order, Purchase Order, Credit Memo, General Journal Batch) is two-phase:
1. REST call sets `postStatus = SCHEDULED` and publishes a message to RabbitMQ exchange `erp.operations.post` with a routing key.
2. A `XxxPostReceiver` bean in `beans/rabbitmq/` consumes from a dedicated queue and runs the actual transactional post logic via `AsyncXxxPostService`.

RabbitMQ config (virtual host, credentials, queue names) is read from `application.properties` via `app.rabbitmq.*` env vars (defaults assume local RabbitMQ with vhost `erp`, user `erp`).

### Frontend internal layers

```
controllers/  →  services/  →  (HTTP calls to backend REST API)
                     ↓
              Thymeleaf templates (resources/templates/)
```

Frontend services mirror the backend domain (e.g. `SalesOrderService` calls `/api/v1/salesorders`). The backend URL is configured via `erp.backendurl` (default `http://localhost:8082`).

Multi-step object creation uses wizard-pattern controllers — each wizard step is a separate controller method and Thymeleaf page (e.g. `salesOrderWizardFirstPage.html`, `salesOrderWizardSecondPage.html`).

### Data flow for posting (end to end)

Browser → Frontend Controller → Frontend Service (HTTP) → Backend Controller → Backend Service → sets `postStatus=SCHEDULED`, publishes to RabbitMQ → `XxxPostReceiver` → `AsyncXxxPostService` (transactional, creates Posted document + ledger entries, deletes source document).

## Key configuration

| Variable | Default | Purpose |
|---|---|---|
| `SRV_PORT` | `8081` (frontend) / `8082` (backend) | HTTP port |
| `ERP_BACKENDURL` | `http://localhost:8082` | Frontend → backend URL |
| `DB_URL` | `jdbc:h2:mem:db` | Database (use MySQL in production) |
| `RMQ_HOSTNAME` | `localhost` | RabbitMQ host |
| `RMQ_VHOST` | `erp` | RabbitMQ virtual host |

Production docker-compose settings live in `docker/env.prod`; dev settings in `docker/env.dev`.
