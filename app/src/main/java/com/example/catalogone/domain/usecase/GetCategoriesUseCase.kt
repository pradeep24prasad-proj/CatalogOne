package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.repository.ProductRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        return repository.getCategories()
    }
}
