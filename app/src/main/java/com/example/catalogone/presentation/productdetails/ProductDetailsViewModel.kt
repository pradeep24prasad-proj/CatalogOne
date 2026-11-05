package com.example.catalogone.presentation.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogone.domain.usecase.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailsUiState.Loading
            val result = getProductDetailsUseCase(productId)
            result.fold(
                onSuccess = { product ->
                    _uiState.value = ProductDetailsUiState.Success(product)
                },
                onFailure = { error ->
                    _uiState.value = ProductDetailsUiState.Error(
                        error.message ?: "Failed to load product details"
                    )
                }
            )
        }
    }
}
