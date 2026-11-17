package dev.shop.orders.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import dev.shop.orders.domain.OrderStatus
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    var id: UUID,
    @Column(name = "customer_id", nullable = false)
    var customerId: String,
    @Column(nullable = false, length = 3)
    var currency: String,
    @Column(name = "total_cents", nullable = false)
    var totalCents: Long,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus,
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
) {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("id")
    var lines: MutableList<OrderLineEntity> = mutableListOf()
}

@Entity
@Table(name = "order_lines")
class OrderLineEntity(
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    var order: OrderEntity,
    @Column(nullable = false)
    var sku: String,
    @Column(nullable = false)
    var quantity: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
