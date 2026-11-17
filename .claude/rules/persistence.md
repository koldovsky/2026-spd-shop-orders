---
paths:
  - "src/main/kotlin/**/persistence/**/*.kt"
  - "src/main/resources/db/migration/**"
---
<!-- owner: @koldovsky · why: entities leaked into the domain twice in Q3 · review-by: 2027-02-19 -->
# Persistence

- Repositories return domain types (`dev.shop.orders.domain.*`), never JPA entities. Map at the adapter boundary.
- Every new query gets a test in `src/test/kotlin/dev/shop/orders/persistence/PersistenceIT.kt`.
- Schema changes: a new Flyway migration `V<n>__<what>.sql`; Hibernate stays on `ddl-auto=validate`.
- Columns: snake_case; money `bigint` cents; time `timestamp with time zone`; every foreign key has an index.
