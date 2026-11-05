package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.repository.ProductRepository
import javax.inject.Inject

class GetFeaturedProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return repository.getFeaturedProducts()
    }
}
