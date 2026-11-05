package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Result<Product> {
        return repository.getProductDetails(id)
    }
}
