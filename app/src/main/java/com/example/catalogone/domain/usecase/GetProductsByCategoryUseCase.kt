package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(category: String): Result<List<Product>> {
        return if (category.equals("all", ignoreCase = true)) {
            repository.getAllProducts()
        } else {
            repository.getProductsByCategory(category)
        }
    }
}
