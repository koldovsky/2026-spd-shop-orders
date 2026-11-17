package dev.shop.orders.support

import dev.shop.orders.domain.OrderLine
import dev.shop.orders.domain.PricingClient

/** 10.00 per unit, so tests can assert totals without shop-pricing running. */
class FakePricingClient : PricingClient {
    override fun quote(currency: String, lines: List<OrderLine>): Long = lines.sumOf { it.quantity * 1000L }
}
