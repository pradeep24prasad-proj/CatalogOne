package com.example.catalogone.presentation.home

import com.example.catalogone.domain.model.Product

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(
        val featuredProducts: List<Product>,
        val categories: List<String>
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
