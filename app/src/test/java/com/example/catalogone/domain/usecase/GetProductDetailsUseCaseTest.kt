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

class GetProductDetailsUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetProductDetailsUseCase

    private val sampleProduct = Product(
        id = 10,
        title = "Headphones",
        price = 49.99,
        description = "Wireless",
        category = "electronics",
        image = "audio.png",
        rating = Rating(rate = 4.5, count = 150)
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetProductDetailsUseCase(repository)
    }

    @Test
    fun `invoke returns product details for given id`() = runTest {
        coEvery { repository.getProductDetails(10) } returns Result.success(sampleProduct)

        val result = useCase(10)

        assertTrue(result.isSuccess)
        assertEquals(10, result.getOrNull()?.id)
        assertEquals("Headphones", result.getOrNull()?.title)
    }
}
