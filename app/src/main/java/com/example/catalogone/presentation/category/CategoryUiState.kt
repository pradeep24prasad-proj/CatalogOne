package com.example.catalogone.presentation.category

import com.example.catalogone.domain.model.Product

sealed interface CategoryUiState {
    object Loading : CategoryUiState
    data class Success(
        val categoryName: String,
        val products: List<Product>
    ) : CategoryUiState
    object Empty : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}
