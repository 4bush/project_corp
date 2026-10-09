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
