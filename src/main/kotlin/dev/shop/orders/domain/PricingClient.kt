package dev.shop.orders.domain

/** Port to shop-pricing. Money is always integer cents. */
interface PricingClient {
    fun quote(currency: String, lines: List<OrderLine>): Long
}
