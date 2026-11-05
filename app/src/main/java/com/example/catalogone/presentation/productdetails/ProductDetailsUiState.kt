package com.example.catalogone.presentation.productdetails

import com.example.catalogone.domain.model.Product

sealed interface ProductDetailsUiState {
    object Loading : ProductDetailsUiState
    data class Success(val product: Product) : ProductDetailsUiState
    data class Error(val message: String) : ProductDetailsUiState
}
