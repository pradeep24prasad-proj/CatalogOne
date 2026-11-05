package com.example.catalogone

import com.example.catalogone.domain.model.Enquiry
import com.example.catalogone.domain.model.EnquiryResult
import com.example.catalogone.domain.repository.EnquiryRepository
import com.example.catalogone.domain.usecase.SubmitEnquiryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubmitEnquiryUseCaseTest {

    private lateinit var enquiryRepository: EnquiryRepository
    private lateinit var useCase: SubmitEnquiryUseCase

    @Before
    fun setUp() {
        enquiryRepository = mockk()
        useCase = SubmitEnquiryUseCase(enquiryRepository)
    }

    @Test
    fun `validate returns error when customer name is blank`() {
        val enquiry = Enquiry(1, "Title", "", "test@example.com", "123456", "Message text")
        val error = useCase.validate(enquiry)
        assertEquals("Customer name is required", error)
    }

    @Test
    fun `validate returns error when email is invalid`() {
        val enquiry = Enquiry(1, "Title", "John", "invalid-email", "123456", "Message text")
        val error = useCase.validate(enquiry)
        assertEquals("Valid email address is required", error)
    }

    @Test
    fun `validate returns error when phone is too short`() {
        val enquiry = Enquiry(1, "Title", "John", "john@example.com", "123", "Message text")
        val error = useCase.validate(enquiry)
        assertEquals("Phone number must be at least 5 characters", error)
    }

    @Test
    fun `validate returns null when all fields are valid`() {
        val enquiry = Enquiry(1, "Title", "John", "john@example.com", "+12345678", "Enquiry message body")
        val error = useCase.validate(enquiry)
        assertNull(error)
    }

    @Test
    fun `invoke fails immediately on validation error without repository call`() = runTest {
        val invalidEnquiry = Enquiry(1, "Title", "", "bad-email", "12", "short")
        val result = useCase(invalidEnquiry)
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `invoke submits enquiry to repository when valid`() = runTest {
        val validEnquiry = Enquiry(1, "Title", "John", "john@example.com", "+12345678", "Enquiry message body")
        coEvery { enquiryRepository.submitEnquiry(validEnquiry) } returns Result.success(
            EnquiryResult(true, "Enquiry submitted", "enq_100")
        )

        val result = useCase(validEnquiry)
        assertTrue(result.isSuccess)
        assertEquals("enq_100", result.getOrNull()?.enquiryId)
    }
}
