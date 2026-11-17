package dev.shop.orders.persistence

import dev.shop.orders.domain.Order
import dev.shop.orders.domain.OrderLine
import dev.shop.orders.domain.OrderStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

/** Runs the Flyway migrations on H2 (PostgreSQL mode) and checks the adapter round-trip. */
@DataJpaTest
@Import(JpaOrderRepository::class)
class PersistenceIT(@Autowired private val repo: JpaOrderRepository) {

    @Test
    fun `saves and loads an order with its lines`() {
        val order = Order(UUID.randomUUID(), "c-9", "UAH", listOf(OrderLine("SKU-1", 3)), 4500, OrderStatus.NEW, Instant.parse("2026-11-19T09:00:00Z"))
        repo.save(order)
        val loaded = repo.findById(order.id)!!
        assertEquals(order.lines, loaded.lines)
        assertEquals(4500, loaded.totalCents)
    }

    @Test
    fun `updates status without touching lines`() {
        val order = repo.save(Order(UUID.randomUUID(), "c-9", "EUR", listOf(OrderLine("SKU-2", 1)), 1000, OrderStatus.NEW, Instant.now()))
        repo.save(order.cancel())
        assertEquals(OrderStatus.CANCELLED, repo.findById(order.id)!!.status)
        assertEquals(1, repo.findById(order.id)!!.lines.size)
    }
}
