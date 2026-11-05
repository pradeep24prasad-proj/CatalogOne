package com.example.catalogone.data.mapper

import com.example.catalogone.data.remote.dto.EnquiryResponseDto
import com.example.catalogone.data.remote.dto.ProductDto
import com.example.catalogone.data.remote.dto.RatingDto
import com.example.catalogone.domain.model.Enquiry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `ProductDto toDomain maps all fields correctly`() {
        val dto = ProductDto(
            id = 1,
            title = "Test Product",
            price = 99.9,
            description = "Desc",
            category = "electronics",
            image = "http://test.png",
            rating = RatingDto(rate = 4.8, count = 200)
        )

        val product = dto.toDomain()

        assertEquals(1, product.id)
        assertEquals("Test Product", product.title)
        assertEquals(99.9, product.price, 0.001)
        assertEquals("Desc", product.description)
        assertEquals("electronics", product.category)
        assertEquals("http://test.png", product.image)
        assertEquals(4.8, product.rating.rate, 0.001)
        assertEquals(200, product.rating.count)
    }

    @Test
    fun `Enquiry toDto maps domain enquiry to request dto`() {
        val enquiry = Enquiry(
            productId = 5,
            productTitle = "SSD",
            customerName = "Jane",
            email = "jane@example.com",
            phone = "1234567890",
            message = "Quote please"
        )

        val dto = enquiry.toDto()

        assertEquals(5, dto.productId)
        assertEquals("SSD", dto.productTitle)
        assertEquals("Jane", dto.customerName)
        assertEquals("jane@example.com", dto.email)
        assertEquals("1234567890", dto.phone)
        assertEquals("Quote please", dto.message)
    }

    @Test
    fun `EnquiryResponseDto toDomain maps response dto to EnquiryResult`() {
        val dto = EnquiryResponseDto(
            success = true,
            message = "Submitted",
            enquiryId = "ENQ-999"
        )

        val result = dto.toDomain()

        assertTrue(result.success)
        assertEquals("Submitted", result.message)
        assertEquals("ENQ-999", result.enquiryId)
    }
}
