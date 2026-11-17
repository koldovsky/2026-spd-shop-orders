---
name: add-endpoint
description: Add or change a REST endpoint in shop-orders end to end (domain, adapter, controller, tests). Use when asked to add or change an API endpoint.
---
<!-- owner: @koldovsky · review-by: 2027-02-19 -->
# Add an endpoint

1. Use case in `domain/OrderService.kt`; business rules stay in the domain.
2. New data access: a method on the `OrderRepository` port, implemented in `persistence/JpaOrderRepository.kt`, returning domain types.
3. Controller method and DTOs in `api/OrderController.kt`; rule violations → 422 problem details.
4. Tests: domain → `OrderServiceTest` (no Spring); HTTP → `OrderApiTest` (MockMvc + `FakePricingClient`); new query → `PersistenceIT`.
5. Run `./gradlew test`, then ask the `reviewer` subagent to check the diff.
