package com.example.catalogone.presentation.enquiry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogone.domain.model.Enquiry
import com.example.catalogone.domain.usecase.GetProductDetailsUseCase
import com.example.catalogone.domain.usecase.SubmitEnquiryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnquiryViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val submitEnquiryUseCase: SubmitEnquiryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EnquiryUiState>(EnquiryUiState.LoadingProduct)
    val uiState: StateFlow<EnquiryUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = EnquiryUiState.LoadingProduct
            val result = getProductDetailsUseCase(productId)
            result.fold(
                onSuccess = { product ->
                    _uiState.value = EnquiryUiState.Content(product = product)
                },
                onFailure = { error ->
                    _uiState.value = EnquiryUiState.Error(
                        error.message ?: "Failed to load product for enquiry"
                    )
                }
            )
        }
    }

    fun submitEnquiry(
        customerName: String,
        email: String,
        phone: String,
        message: String
    ) {
        val currentState = _uiState.value as? EnquiryUiState.Content ?: return

        val enquiry = Enquiry(
            productId = currentState.product.id,
            productTitle = currentState.product.title,
            customerName = customerName.trim(),
            email = email.trim(),
            phone = phone.trim(),
            message = message.trim()
        )

        val validationError = submitEnquiryUseCase.validate(enquiry)
        if (validationError != null) {
            _uiState.value = currentState.copy(
                customerName = customerName,
                email = email,
                phone = phone,
                message = message,
                validationError = validationError,
                errorMessage = null
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(
                customerName = customerName,
                email = email,
                phone = phone,
                message = message,
                isSubmitting = true,
                validationError = null,
                errorMessage = null
            )

            val result = submitEnquiryUseCase(enquiry)
            result.fold(
                onSuccess = {
                    _uiState.value = EnquiryUiState.SubmittedSuccess(
                        productTitle = currentState.product.title,
                        customerName = customerName.trim()
                    )
                },
                onFailure = { error ->
                    _uiState.value = currentState.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Failed to submit enquiry. Please try again."
                    )
                }
            )
        }
    }
}
