package com.example.catalogone.data.repository

import com.example.catalogone.data.remote.CatalogApiService
import com.example.catalogone.data.remote.dto.EnquiryResponseDto
import com.example.catalogone.domain.model.Enquiry
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EnquiryRepositoryImplTest {

    private lateinit var apiService: CatalogApiService
    private lateinit var repository: EnquiryRepositoryImpl

    private val sampleEnquiry = Enquiry(
        productId = 1,
        productTitle = "Test Product",
        customerName = "Jane Doe",
        email = "jane@example.com",
        phone = "1234567890",
        message = "Interested"
    )

    @Before
    fun setUp() {
        apiService = mockk()
        repository = EnquiryRepositoryImpl(apiService)
    }

    @Test
    fun `submitEnquiry returns success result on api success`() = runTest {
        val mockResponse = EnquiryResponseDto(
            success = true,
            message = "Enquiry submitted successfully",
            enquiryId = "ENQ-123"
        )
        coEvery { apiService.submitEnquiry(any()) } returns mockResponse

        val result = repository.submitEnquiry(sampleEnquiry)

        assertTrue(result.isSuccess)
        val enquiryResult = result.getOrNull()
        assertEquals("ENQ-123", enquiryResult?.enquiryId)
        assertEquals("Enquiry submitted successfully", enquiryResult?.message)
    }
}
