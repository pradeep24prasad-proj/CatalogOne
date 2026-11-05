package com.example.catalogone.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogone.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Initial
            return
        }

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            val result = searchProductsUseCase(query)
            result.fold(
                onSuccess = { products ->
                    if (products.isEmpty()) {
                        _uiState.value = SearchUiState.Empty
                    } else {
                        _uiState.value = SearchUiState.Success(
                            query = query,
                            products = products
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.value = SearchUiState.Error(
                        error.message ?: "Failed to perform product search"
                    )
                }
            )
        }
    }

    fun clearSearch() {
        _uiState.value = SearchUiState.Initial
    }
}
