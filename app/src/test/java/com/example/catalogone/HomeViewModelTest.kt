package com.example.catalogone

import app.cash.turbine.test
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating
import com.example.catalogone.domain.usecase.GetCategoriesUseCase
import com.example.catalogone.domain.usecase.GetFeaturedProductsUseCase
import com.example.catalogone.presentation.home.HomeScreen
import com.example.catalogone.presentation.home.HomeUiState
import com.example.catalogone.presentation.home.HomeViewModel
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
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getFeaturedProductsUseCase: GetFeaturedProductsUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    private val sampleProducts = listOf(
        Product(1, "Prod A", 10.0, "Desc A", "cat1", "img1", Rating(4.9, 100)),
        Product(2, "Prod B", 20.0, "Desc B", "cat2", "img2", Rating(4.8, 80))
    )
    private val sampleCategories = listOf("cat1", "cat2")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getFeaturedProductsUseCase = mockk()
        getCategoriesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadHomeData updates uiState to Success when use cases return data`() = runTest {
        coEvery { getFeaturedProductsUseCase() } returns Result.success(sampleProducts)
        coEvery { getCategoriesUseCase() } returns Result.success(sampleCategories)

        val viewModel = HomeViewModel(getFeaturedProductsUseCase, getCategoriesUseCase)

        viewModel.uiState.test {
            // Initial emit is Loading
            assertEquals(HomeUiState.Loading, awaitItem())

            testDispatcher.scheduler.advanceUntilIdle()

            val successItem = awaitItem()
            assertTrue(successItem is HomeUiState.Success)
            val state = successItem as HomeUiState.Success
            assertEquals(2, state.featuredProducts.size)
            assertEquals(listOf("cat1", "cat2"), state.categories)
        }
    }

    @Test
    fun `loadHomeData updates uiState to Error when use case fails`() = runTest {
        coEvery { getFeaturedProductsUseCase() } returns Result.failure(Exception("Network failure"))
        coEvery { getCategoriesUseCase() } returns Result.success(sampleCategories)

        val viewModel = HomeViewModel(getFeaturedProductsUseCase, getCategoriesUseCase)

        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())

            testDispatcher.scheduler.advanceUntilIdle()

            val errorItem = awaitItem()
            assertTrue(errorItem is HomeUiState.Error)
            assertEquals("Network failure", (errorItem as HomeUiState.Error).message)
        }
    }
}
