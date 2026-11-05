package com.example.catalogone

import app.cash.turbine.test
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating
import com.example.catalogone.domain.usecase.GetProductsByCategoryUseCase
import com.example.catalogone.presentation.category.CategoryUiState
import com.example.catalogone.presentation.category.CategoryViewModel
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
class CategoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getProductsByCategoryUseCase: GetProductsByCategoryUseCase

    private val sampleProducts = listOf(
        Product(1, "Prod A", 10.0, "Desc A", "electronics", "img1", Rating(4.9, 100))
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getProductsByCategoryUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCategoryProducts emits Success with category products`() = runTest {
        coEvery { getProductsByCategoryUseCase("electronics") } returns Result.success(sampleProducts)

        val viewModel = CategoryViewModel(getProductsByCategoryUseCase)

        viewModel.uiState.test {
            assertEquals(CategoryUiState.Loading, awaitItem())

            viewModel.loadCategoryProducts("electronics")
            testDispatcher.scheduler.advanceUntilIdle()

            val successState = awaitItem() as CategoryUiState.Success
            assertEquals("electronics", successState.categoryName)
            assertEquals(1, successState.products.size)
            assertEquals("Prod A", successState.products[0].title)
        }
    }

    @Test
    fun `loadCategoryProducts emits Empty when no products returned`() = runTest {
        coEvery { getProductsByCategoryUseCase("empty_cat") } returns Result.success(emptyList())

        val viewModel = CategoryViewModel(getProductsByCategoryUseCase)

        viewModel.uiState.test {
            assertEquals(CategoryUiState.Loading, awaitItem())

            viewModel.loadCategoryProducts("empty_cat")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is CategoryUiState.Empty)
        }
    }
}
