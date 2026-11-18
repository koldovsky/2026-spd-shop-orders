---
paths:
  - "src/main/kotlin/**/api/**/*.kt"
---
<!-- owner: @koldovsky · why: consistent error contract for shop-web · review-by: 2027-02-19 -->
# API errors (local copy — shop-orders version)

- Errors are RFC 9457 problem details: 404 not found, 422 business rule violation, 400 malformed request. Never a 500 for validation.
- Every endpoint has a MockMvc test in `src/test/kotlin/dev/shop/orders/api/` using `FakePricingClient`.
- Breaking changes to a response shape need a note in 2026-spd-shop-workspace `docs/contracts.md`.
