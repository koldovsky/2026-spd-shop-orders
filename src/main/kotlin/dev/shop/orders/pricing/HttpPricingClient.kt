package dev.shop.orders.pricing

import dev.shop.orders.domain.OrderLine
import dev.shop.orders.domain.PricingClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

/** Calls shop-pricing: POST /quote → { "totalCents": 1234 }. Contract: shop-workspace/docs/contracts.md#pricing */
@Component
class HttpPricingClient(@Value("\${shop.pricing.base-url}") baseUrl: String) : PricingClient {

    private val http = RestClient.builder().baseUrl(baseUrl).build()

    override fun quote(currency: String, lines: List<OrderLine>): Long {
        val response = http.post()
            .uri("/quote")
            .body(QuoteRequest(currency, lines.map { QuoteLine(it.sku, it.quantity) }))
            .retrieve()
            .body<QuoteResponse>()
        return requireNotNull(response) { "empty response from shop-pricing" }.totalCents
    }

    data class QuoteLine(val sku: String, val quantity: Int)
    data class QuoteRequest(val currency: String, val lines: List<QuoteLine>)
    data class QuoteResponse(val totalCents: Long)
}
