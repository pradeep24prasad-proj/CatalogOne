package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCategoriesUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetCategoriesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCategoriesUseCase(repository)
    }

    @Test
    fun `invoke calls repository getCategories and returns result`() = runTest {
        val categories = listOf("electronics", "jewelery")
        coEvery { repository.getCategories() } returns Result.success(categories)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(categories, result.getOrNull())
    }
}
