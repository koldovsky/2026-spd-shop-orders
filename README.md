# shop-orders

Orders service of the demo **Shop** system used in the SPD «Team AI Engineering» workshop (Workshop 1).
Kotlin + Spring Boot 4, PostgreSQL in compose, in-memory H2 locally (no Docker needed).

| | |
|---|---|
| Build and test | `./gradlew test` (Windows: `gradlew.bat test`) |
| Run locally | `./gradlew bootRun` → http://localhost:8081 |
| Needs | JDK 21; `shop-pricing` on :8082 only for `POST /orders` |

Related repos: `shop-workspace` (system overview, contracts, compose), `shop-pricing`, `shop-web`, `shop-harness` (shared agent harness).

## Branches

- `main` — the **ad-hoc** start state, the way many repos look today: one long instruction file, a stale skill, nothing enforced.
- `governed` — the end state of Demo 1: short `AGENTS.md`, a path rule, committed permissions, a blocking hook, CODEOWNERS, team agreement.

Compare them: `git diff main governed --stat`.

Demo code, not production code. All credentials in this repo are fake.
