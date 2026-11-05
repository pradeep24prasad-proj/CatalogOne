package com.example.catalogone.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogone.domain.usecase.GetProductsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun loadCategoryProducts(categoryName: String) {
        viewModelScope.launch {
            _uiState.value = CategoryUiState.Loading
            val result = getProductsByCategoryUseCase(categoryName)
            result.fold(
                onSuccess = { products ->
                    if (products.isEmpty()) {
                        _uiState.value = CategoryUiState.Empty
                    } else {
                        _uiState.value = CategoryUiState.Success(
                            categoryName = categoryName,
                            products = products
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.value = CategoryUiState.Error(
                        error.message ?: "Failed to load category products"
                    )
                }
            )
        }
    }
}
