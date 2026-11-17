package dev.shop.orders.api

import dev.shop.orders.domain.PricingClient
import dev.shop.orders.support.FakePricingClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiTest(@Autowired private val mvc: MockMvc) {

    @TestConfiguration
    class Fakes {
        @Bean
        @Primary
        fun pricing(): PricingClient = FakePricingClient()
    }

    @Test
    fun `POST orders returns 201 with the priced total`() {
        mvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"customerId":"c-1","currency":"EUR","lines":[{"sku":"SKU-1","quantity":2}]}"""
        }.andExpect {
            status { isCreated() }
            jsonPath("$.totalCents") { value(2000) }
            jsonPath("$.status") { value("NEW") }
        }
    }

    @Test
    fun `invalid quantity is a 422 problem`() {
        mvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"customerId":"c-1","currency":"EUR","lines":[{"sku":"SKU-1","quantity":0}]}"""
        }.andExpect {
            status { isUnprocessableContent() }
        }
    }
}
