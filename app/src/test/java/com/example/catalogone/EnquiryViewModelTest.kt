package com.example.catalogone

import app.cash.turbine.test
import com.example.catalogone.domain.model.Enquiry
import com.example.catalogone.domain.model.EnquiryResult
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating
import com.example.catalogone.domain.usecase.GetProductDetailsUseCase
import com.example.catalogone.domain.usecase.SubmitEnquiryUseCase
import com.example.catalogone.presentation.enquiry.EnquiryUiState
import com.example.catalogone.presentation.enquiry.EnquiryViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EnquiryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getProductDetailsUseCase: GetProductDetailsUseCase
    private lateinit var submitEnquiryUseCase: SubmitEnquiryUseCase

    private val sampleProduct = Product(
        id = 1,
        title = "Backpack",
        price = 100.0,
        description = "Durable backpack",
        category = "travel",
        image = "img.png",
        rating = Rating(4.7, 30)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getProductDetailsUseCase = mockk()
        submitEnquiryUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submitEnquiry with validation error updates Content state with validationError`() = runTest {
        coEvery { getProductDetailsUseCase(1) } returns Result.success(sampleProduct)
        every { submitEnquiryUseCase.validate(any()) } returns "Customer name is required"

        val viewModel = EnquiryViewModel(getProductDetailsUseCase, submitEnquiryUseCase)
        viewModel.loadProduct(1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.submitEnquiry("", "jane@example.com", "123456", "Hello message")

        val state = viewModel.uiState.value as EnquiryUiState.Content
        assertEquals("Customer name is required", state.validationError)
    }

    @Test
    fun `submitEnquiry with valid inputs emits SubmittedSuccess`() = runTest {
        coEvery { getProductDetailsUseCase(1) } returns Result.success(sampleProduct)
        every { submitEnquiryUseCase.validate(any()) } returns null
        coEvery { submitEnquiryUseCase(any()) } returns Result.success(
            EnquiryResult(true, "Enquiry submitted", "enq_123")
        )

        val viewModel = EnquiryViewModel(getProductDetailsUseCase, submitEnquiryUseCase)
        viewModel.loadProduct(1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val contentItem = awaitItem() as EnquiryUiState.Content
            assertEquals("Backpack", contentItem.product.title)

            viewModel.submitEnquiry("Jane Doe", "jane@example.com", "123456", "Hello message")
            testDispatcher.scheduler.advanceUntilIdle()

            val submittingState = awaitItem() as EnquiryUiState.Content
            assertTrue(submittingState.isSubmitting)

            val successState = awaitItem() as EnquiryUiState.SubmittedSuccess
            assertEquals("Backpack", successState.productTitle)
            assertEquals("Jane Doe", successState.customerName)
        }
    }
}
