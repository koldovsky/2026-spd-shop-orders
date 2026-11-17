<!-- owner: @koldovsky (orders team) · why: instructions every agent needs in every session · review-by: 2027-02-19 -->
# shop-orders

Orders service of the Shop system. Kotlin + Spring Boot 4, Flyway, PostgreSQL (H2 in PostgreSQL mode locally).
System overview, service map and contracts: the `shop-workspace` repo.

## Commands
- Test: `./gradlew test` (Windows: `gradlew.bat test`) — run before you say a task is done.
- Run: `./gradlew bootRun` → :8081. `POST /orders` needs shop-pricing on :8082.

## Hard rules
- Applied migrations in `src/main/resources/db/migration/` are immutable. Add a new `V<n>__<what>.sql`. (Enforced by a hook.)
- Money is `Long` cents and is priced only by shop-pricing. Never compute prices here.
- `.env` and `secrets/` are off limits. (Enforced by permissions.)

## Where things are
- Business rules: `domain/` — no Spring Web, no JPA types.
- Persistence, API and pricing adapters: `persistence/`, `api/`, `pricing/`. Path rules in `.claude/rules/` load when you touch them.
- Team agreement and autonomy matrix: `docs/ai/team-agreement.md`.

## Done means
Tests green, PR under 400 changed lines, PR template filled in, commit message `SHOP-<n>: <imperative summary>`.
