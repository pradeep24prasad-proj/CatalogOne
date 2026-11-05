package com.example.catalogone.presentation.search

import com.example.catalogone.domain.model.Product

sealed interface SearchUiState {
    object Initial : SearchUiState
    object Loading : SearchUiState
    data class Success(
        val query: String,
        val products: List<Product>
    ) : SearchUiState
    object Empty : SearchUiState
    data class Error(val message: String) : SearchUiState
}
