---
name: add-endpoint
description: Add a new REST endpoint to shop-orders end to end (controller, service, persistence, tests). Use when asked to add or change an API endpoint.
---

# Add an endpoint

1. Add the use case to `domain/OrderService.kt`. Business rules live in the domain, not in the controller.
2. If it needs new data access, add a method to the `OrderRepository` port and implement it in
   `persistence/JpaOrderRepository.kt`. Return domain types, never entities.
3. Add the controller method in `api/OrderController.kt` with request/response DTOs. Map rule violations to 422.
4. Tests:
   - domain behaviour → `src/test/kotlin/.../domain/OrderServiceTest.kt` (no Spring);
   - HTTP shape → `src/test/kotlin/.../api/OrderApiTest.kt` (MockMvc, `FakePricingClient`);
   - new query → `src/test/kotlin/.../persistence/PersistenceIT.kt`.
5. Run `./gradlew test` and report the result.
