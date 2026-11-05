package com.example.catalogone.presentation.enquiry

import com.example.catalogone.domain.model.Product

sealed interface EnquiryUiState {
    object LoadingProduct : EnquiryUiState
    data class Content(
        val product: Product,
        val customerName: String = "",
        val email: String = "",
        val phone: String = "",
        val message: String = "",
        val validationError: String? = null,
        val isSubmitting: Boolean = false,
        val errorMessage: String? = null
    ) : EnquiryUiState
    data class SubmittedSuccess(
        val productTitle: String,
        val customerName: String
    ) : EnquiryUiState
    data class Error(val message: String) : EnquiryUiState
}
