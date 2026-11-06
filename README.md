# Purchases — purchase request tracker

## Product
We build a purchase request tracker. People track a **purchase request** from draft to order.

## Core item
`PurchaseRequest` — one tracked item with a surrogate id (`RequestId`, UUID) and a human
business key (`PR-19`). Table: `purchase_request`.

## Status table
Statuses: `DRAFT`, `APPROVED`, `ORDERED`.

| # | From | To | Result |
| - | ---- | -- | ------ |
| 1 | DRAFT | APPROVED | allowed |
| 2 | APPROVED | ORDERED | allowed |
| 3 | DRAFT | ORDERED | **forbidden** |
| 4 | ORDERED | APPROVED | **forbidden** |

## Forbidden — why
- **DRAFT → ORDERED**: approval was skipped; money would be spent on an unapproved request.
- **ORDERED → APPROVED**: the order is already placed at the supplier; going back would lie about the state.

## Packages (arrows point inward)

```
  dto          client        handler         config
  (JSON later) (HTTP later)  (HTTP week 9)   Application, AppConfig
                                             RequestService (@Service)
       \            \            /                |
        \            \          /           injects Rule
         \            \        /
                     domain                   persistence
        RequestId  RequestStatus  RequestPolicy   RequestJdbc (JdbcTemplate)
        Rule + TransitionRule + UnapprovedCannotOrder (+ ChainRule)
        DuplicateRequest, SupplierOrderPort, OrderPlacement
                     (no Spring, no JDBC)
```

## Run
```bash
docker compose up -d        # only PostgreSQL (css/css on localhost:5432)
mvn -q verify               # tests against PostgreSQL
mvn spring-boot:run         # starts the app
```
`src/main/resources/db/schema.sql` is the hand-written schema; the tests drop and re-apply it.

## HTTP API
The service exposes `POST /api/v1/purchase-requests` with a JSON body containing
`businessKey` and `title`. A successful request returns `201 Created` with its generated
UUID, `DRAFT` status, business key, and title. Retrieve it with
`GET /api/v1/purchase-requests/{id}`. Invalid input returns `400`, an unknown UUID returns
`404`, and a duplicate business key returns `409`.

OpenAPI UI: `/swagger-ui/index.html`; health check: `/actuator/health`.
