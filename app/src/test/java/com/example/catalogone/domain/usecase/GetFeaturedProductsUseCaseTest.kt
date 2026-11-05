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

class GetFeaturedProductsUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetFeaturedProductsUseCase

    private val sampleProduct = Product(
        id = 1,
        title = "SSD 256GB",
        price = 109.0,
        description = "High speed",
        category = "electronics",
        image = "ssd.png",
        rating = Rating(rate = 4.8, count = 300)
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetFeaturedProductsUseCase(repository)
    }

    @Test
    fun `invoke calls repository getFeaturedProducts and returns list`() = runTest {
        coEvery { repository.getFeaturedProducts() } returns Result.success(listOf(sampleProduct))

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("SSD 256GB", result.getOrNull()?.first()?.title)
    }
}
