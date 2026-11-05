package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating
import com.example.catalogone.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchProductsUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: SearchProductsUseCase

    private val sampleProduct = Product(
        id = 3,
        title = "Gold Chain",
        price = 299.0,
        description = "Jewelry",
        category = "jewelery",
        image = "gold.png",
        rating = Rating(rate = 4.9, count = 50)
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchProductsUseCase(repository)
    }

    @Test
    fun `invoke calls repository searchProducts and returns result`() = runTest {
        coEvery { repository.searchProducts("Gold") } returns Result.success(listOf(sampleProduct))

        val result = useCase("Gold")

        assertTrue(result.isSuccess)
        assertEquals("Gold Chain", result.getOrNull()?.first()?.title)
    }
}
