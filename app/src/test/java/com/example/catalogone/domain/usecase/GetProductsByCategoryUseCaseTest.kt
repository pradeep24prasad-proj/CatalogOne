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

class GetProductsByCategoryUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetProductsByCategoryUseCase

    private val sampleProduct = Product(
        id = 2,
        title = "Jacket",
        price = 89.0,
        description = "Leather",
        category = "men's clothing",
        image = "jacket.png",
        rating = Rating(rate = 4.7, count = 80)
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetProductsByCategoryUseCase(repository)
    }

    @Test
    fun `invoke calls repository getProductsByCategory and returns result`() = runTest {
        coEvery { repository.getProductsByCategory("men's clothing") } returns Result.success(listOf(sampleProduct))

        val result = useCase("men's clothing")

        assertTrue(result.isSuccess)
        assertEquals("Jacket", result.getOrNull()?.first()?.title)
    }
}
