package com.example.catalogone

import app.cash.turbine.test
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating
import com.example.catalogone.domain.usecase.SearchProductsUseCase
import com.example.catalogone.presentation.search.SearchUiState
import com.example.catalogone.presentation.search.SearchViewModel
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
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var searchProductsUseCase: SearchProductsUseCase

    private val sampleProducts = listOf(
        Product(1, "Phone X", 999.0, "Flagship phone", "electronics", "img1", Rating(4.9, 50))
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        searchProductsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search emits Success with matching products`() = runTest {
        coEvery { searchProductsUseCase("phone") } returns Result.success(sampleProducts)

        val viewModel = SearchViewModel(searchProductsUseCase)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Initial, awaitItem())

            viewModel.search("phone")
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem()
            assertTrue(loadingState is SearchUiState.Loading)

            val successState = awaitItem() as SearchUiState.Success
            assertEquals("phone", successState.query)
            assertEquals(1, successState.products.size)
            assertEquals("Phone X", successState.products[0].title)
        }
    }

    @Test
    fun `search with empty query sets Initial state`() = runTest {
        val viewModel = SearchViewModel(searchProductsUseCase)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Initial, awaitItem())

            viewModel.search("  ")
            testDispatcher.scheduler.advanceUntilIdle()

            // State remains Initial
            expectNoEvents()
        }
    }
}
