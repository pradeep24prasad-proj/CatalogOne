package com.example.catalogone

import app.cash.turbine.test
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating
import com.example.catalogone.domain.usecase.GetProductDetailsUseCase
import com.example.catalogone.presentation.productdetails.ProductDetailsUiState
import com.example.catalogone.presentation.productdetails.ProductDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getProductDetailsUseCase: GetProductDetailsUseCase

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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProductDetails emits Success when product found`() = runTest {
        coEvery { getProductDetailsUseCase(1) } returns Result.success(sampleProduct)

        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase)

        viewModel.uiState.test {
            assertEquals(ProductDetailsUiState.Loading, awaitItem())

            viewModel.loadProductDetails(1)
            testDispatcher.scheduler.advanceUntilIdle()

            val successState = awaitItem() as ProductDetailsUiState.Success
            assertEquals("Backpack", successState.product.title)
        }
    }

    @Test
    fun `loadProductDetails emits Error when product not found`() = runTest {
        coEvery { getProductDetailsUseCase(999) } returns Result.failure(Exception("Product not found"))

        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase)

        viewModel.uiState.test {
            assertEquals(ProductDetailsUiState.Loading, awaitItem())

            viewModel.loadProductDetails(999)
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem() as ProductDetailsUiState.Error
            assertEquals("Product not found", errorState.message)
        }
    }
}
