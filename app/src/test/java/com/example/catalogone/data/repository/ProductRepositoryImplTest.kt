package com.example.catalogone.data.repository

import com.example.catalogone.data.remote.CatalogApiService
import com.example.catalogone.data.remote.dto.ProductDto
import com.example.catalogone.data.remote.dto.RatingDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var apiService: CatalogApiService
    private lateinit var repository: ProductRepositoryImpl

    private val sampleDto = ProductDto(
        id = 1,
        title = "Test Product",
        price = 99.99,
        description = "Test Desc",
        category = "electronics",
        image = "test.png",
        rating = RatingDto(rate = 4.5, count = 100)
    )

    @Before
    fun setUp() {
        apiService = mockk()
        repository = ProductRepositoryImpl(apiService)
    }

    @Test
    fun `getFeaturedProducts returns mapped domain products on success`() = runTest {
        coEvery { apiService.getFeaturedProducts() } returns listOf(sampleDto)

        val result = repository.getFeaturedProducts()

        assertTrue(result.isSuccess)
        val products = result.getOrNull()
        assertEquals(1, products?.size)
        assertEquals("Test Product", products?.first()?.title)
    }

    @Test
    fun `getCategories returns list of categories on success`() = runTest {
        coEvery { apiService.getCategories() } returns listOf("electronics", "jewelery")

        val result = repository.getCategories()

        assertTrue(result.isSuccess)
        assertEquals(listOf("electronics", "jewelery"), result.getOrNull())
    }

    @Test
    fun `getProductsByCategory returns mapped domain products on success`() = runTest {
        coEvery { apiService.getProductsByCategory("electronics") } returns listOf(sampleDto)

        val result = repository.getProductsByCategory("electronics")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals(1, result.getOrNull()?.first()?.id)
    }

    @Test
    fun `searchProducts returns mapped domain products on query match`() = runTest {
        coEvery { apiService.searchProducts("phone") } returns listOf(sampleDto)

        val result = repository.searchProducts("phone")

        assertTrue(result.isSuccess)
        assertEquals("Test Product", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `getProductDetails returns mapped domain product for valid id`() = runTest {
        coEvery { apiService.getProductDetails(1) } returns sampleDto

        val result = repository.getProductDetails(1)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.id)
        assertEquals(4.5, result.getOrNull()?.rating?.rate ?: 0.0, 0.01)
    }
}
