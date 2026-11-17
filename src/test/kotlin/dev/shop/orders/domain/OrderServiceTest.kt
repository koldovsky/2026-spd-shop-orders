package dev.shop.orders.domain

import dev.shop.orders.support.FakePricingClient
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OrderServiceTest {

    private val repo = InMemoryOrders()
    private val service = OrderService(repo, FakePricingClient(), Clock.fixed(Instant.parse("2026-11-19T09:00:00Z"), ZoneOffset.UTC))

    @Test
    fun `places an order priced by shop-pricing`() {
        val order = service.place("c-1", "EUR", listOf(OrderLine("SKU-1", 2), OrderLine("SKU-2", 1)))
        assertEquals(3000, order.totalCents)
        assertEquals(OrderStatus.NEW, order.status)
    }

    @Test
    fun `rejects an empty order`() {
        assertFailsWith<OrderRuleViolation> { service.place("c-1", "EUR", emptyList()) }
    }

    @Test
    fun `rejects quantity above the limit`() {
        assertFailsWith<OrderRuleViolation> { service.place("c-1", "EUR", listOf(OrderLine("SKU-1", 101))) }
    }

    @Test
    fun `cancels only NEW orders`() {
        val order = service.place("c-1", "EUR", listOf(OrderLine("SKU-1", 1)))
        service.confirm(order.id)
        assertFailsWith<OrderRuleViolation> { service.cancel(order.id) }
    }

    private class InMemoryOrders : OrderRepository {
        private val store = mutableMapOf<UUID, Order>()
        override fun save(order: Order) = order.also { store[it.id] = it }
        override fun findById(id: UUID) = store[id]
        override fun findByCustomer(customerId: String) = store.values.filter { it.customerId == customerId }
    }
}
