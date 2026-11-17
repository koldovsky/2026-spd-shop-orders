package dev.shop.orders.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderJpaRepository : JpaRepository<OrderEntity, UUID> {
    fun findByCustomerIdOrderByCreatedAtDesc(customerId: String): List<OrderEntity>
}
