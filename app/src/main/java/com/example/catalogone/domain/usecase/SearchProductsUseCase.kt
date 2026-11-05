package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.repository.ProductRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(query: String): Result<List<Product>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return repository.searchProducts(query)
    }
}
