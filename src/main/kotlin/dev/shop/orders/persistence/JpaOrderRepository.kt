package dev.shop.orders.persistence

import dev.shop.orders.domain.Order
import dev.shop.orders.domain.OrderLine
import dev.shop.orders.domain.OrderRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/** Adapter: maps JPA entities to domain types at the boundary. */
@Repository
class JpaOrderRepository(private val jpa: OrderJpaRepository) : OrderRepository {

    override fun save(order: Order): Order {
        val entity = jpa.findById(order.id).orElse(null)?.apply {
            status = order.status
            totalCents = order.totalCents
        } ?: order.toEntity()
        return jpa.save(entity).toDomain()
    }

    override fun findById(id: UUID): Order? = jpa.findById(id).orElse(null)?.toDomain()

    override fun findByCustomer(customerId: String): List<Order> =
        jpa.findByCustomerIdOrderByCreatedAtDesc(customerId).map { it.toDomain() }
}

private fun Order.toEntity(): OrderEntity {
    val entity = OrderEntity(id, customerId, currency, totalCents, status, createdAt)
    entity.lines.addAll(lines.map { OrderLineEntity(entity, it.sku, it.quantity) })
    return entity
}

private fun OrderEntity.toDomain() = Order(
    id = id,
    customerId = customerId,
    currency = currency,
    lines = lines.map { OrderLine(it.sku, it.quantity) },
    totalCents = totalCents,
    status = status,
    createdAt = createdAt,
)
