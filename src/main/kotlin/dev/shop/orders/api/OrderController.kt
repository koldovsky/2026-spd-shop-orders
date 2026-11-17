package dev.shop.orders.api

import dev.shop.orders.domain.Order
import dev.shop.orders.domain.OrderLine
import dev.shop.orders.domain.OrderNotFound
import dev.shop.orders.domain.OrderRuleViolation
import dev.shop.orders.domain.OrderService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/orders")
class OrderController(private val service: OrderService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun place(@Valid @RequestBody request: PlaceOrderRequest): OrderResponse =
        service.place(request.customerId, request.currency, request.lines.map { OrderLine(it.sku, it.quantity) }).toResponse()

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): OrderResponse = service.get(id).toResponse()

    @GetMapping
    fun forCustomer(@RequestParam customerId: String): List<OrderResponse> = service.forCustomer(customerId).map { it.toResponse() }

    @PostMapping("/{id}/cancel")
    fun cancel(@PathVariable id: UUID): OrderResponse = service.cancel(id).toResponse()

    @PostMapping("/{id}/confirm")
    fun confirm(@PathVariable id: UUID): OrderResponse = service.confirm(id).toResponse()

    @ExceptionHandler(OrderNotFound::class)
    fun notFound(e: OrderNotFound): ProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "not found")

    @ExceptionHandler(OrderRuleViolation::class)
    fun ruleViolation(e: OrderRuleViolation): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.message ?: "rule violation")
}

data class LineRequest(@field:NotBlank val sku: String, val quantity: Int)

data class PlaceOrderRequest(
    @field:NotBlank val customerId: String,
    @field:Pattern(regexp = "[A-Z]{3}") val currency: String,
    val lines: List<LineRequest>,
)

data class OrderResponse(
    val id: UUID,
    val customerId: String,
    val currency: String,
    val lines: List<LineRequest>,
    val totalCents: Long,
    val status: String,
    val createdAt: String,
)

private fun Order.toResponse() = OrderResponse(
    id, customerId, currency, lines.map { LineRequest(it.sku, it.quantity) }, totalCents, status.name, createdAt.toString(),
)
