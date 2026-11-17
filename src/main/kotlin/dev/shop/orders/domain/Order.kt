package dev.shop.orders.domain

import java.time.Instant
import java.util.UUID

enum class OrderStatus { NEW, CONFIRMED, CANCELLED }

data class OrderLine(val sku: String, val quantity: Int)

data class Order(
    val id: UUID,
    val customerId: String,
    val currency: String,
    val lines: List<OrderLine>,
    val totalCents: Long,
    val status: OrderStatus,
    val createdAt: Instant,
) {
    fun cancel(): Order {
        if (status != OrderStatus.NEW) throw OrderRuleViolation("only NEW orders can be cancelled, this one is $status")
        return copy(status = OrderStatus.CANCELLED)
    }

    fun confirm(): Order {
        if (status != OrderStatus.NEW) throw OrderRuleViolation("only NEW orders can be confirmed, this one is $status")
        return copy(status = OrderStatus.CONFIRMED)
    }
}

class OrderRuleViolation(message: String) : RuntimeException(message)

class OrderNotFound(id: UUID) : RuntimeException("order $id not found")
