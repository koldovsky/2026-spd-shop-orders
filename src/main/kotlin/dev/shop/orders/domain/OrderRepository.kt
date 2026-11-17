package dev.shop.orders.domain

import java.util.UUID

/** Port: the domain never sees JPA entities. */
interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: UUID): Order?
    fun findByCustomer(customerId: String): List<Order>
}
