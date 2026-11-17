package dev.shop.orders.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Instant
import java.util.UUID

@Service
class OrderService(
    private val orders: OrderRepository,
    private val pricing: PricingClient,
    private val clock: Clock,
) {
    @Transactional
    fun place(customerId: String, currency: String, lines: List<OrderLine>): Order {
        if (lines.isEmpty()) throw OrderRuleViolation("an order needs at least one line")
        if (lines.size > MAX_LINES) throw OrderRuleViolation("at most $MAX_LINES lines per order")
        lines.forEach {
            if (it.quantity !in 1..MAX_QUANTITY) throw OrderRuleViolation("quantity for ${it.sku} must be 1..$MAX_QUANTITY")
        }
        val total = pricing.quote(currency, lines)
        val order = Order(
            id = UUID.randomUUID(),
            customerId = customerId,
            currency = currency,
            lines = lines,
            totalCents = total,
            status = OrderStatus.NEW,
            createdAt = Instant.now(clock),
        )
        return orders.save(order)
    }

    fun get(id: UUID): Order = orders.findById(id) ?: throw OrderNotFound(id)

    fun forCustomer(customerId: String): List<Order> = orders.findByCustomer(customerId)

    @Transactional
    fun cancel(id: UUID): Order = orders.save(get(id).cancel())

    @Transactional
    fun confirm(id: UUID): Order = orders.save(get(id).confirm())

    companion object {
        const val MAX_LINES = 50
        const val MAX_QUANTITY = 100
    }
}
