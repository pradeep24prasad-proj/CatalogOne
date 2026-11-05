package com.example.catalogone.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogone.domain.usecase.GetCategoriesUseCase
import com.example.catalogone.domain.usecase.GetFeaturedProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val featuredDeferred = async { getFeaturedProductsUseCase() }
            val categoriesDeferred = async { getCategoriesUseCase() }

            val featuredResult = featuredDeferred.await()
            val categoriesResult = categoriesDeferred.await()

            if (featuredResult.isSuccess && categoriesResult.isSuccess) {
                _uiState.value = HomeUiState.Success(
                    featuredProducts = featuredResult.getOrDefault(emptyList()),
                    categories = categoriesResult.getOrDefault(emptyList())
                )
            } else {
                val errorMsg = featuredResult.exceptionOrNull()?.message
                    ?: categoriesResult.exceptionOrNull()?.message
                    ?: "Failed to load home catalog data"
                _uiState.value = HomeUiState.Error(errorMsg)
            }
        }
    }
}
