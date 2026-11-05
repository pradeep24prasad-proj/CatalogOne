package com.example.catalogone.presentation.search

import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.example.catalogone.databinding.ScreenSearchBinding
import com.example.catalogone.presentation.components.ProductAdapter

@Composable
fun SearchScreen(
    initialQuery: String,
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            viewModel.search(initialQuery)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        modifier = modifier.statusBarsPadding(),
        factory = { context ->
            val binding = ScreenSearchBinding.inflate(LayoutInflater.from(context))

            val productAdapter = ProductAdapter(
                onProductClick = { productId ->
                    onProductClick(productId)
                }
            )
            binding.rvSearchProducts.adapter = productAdapter

            binding.etSearchQuery.setText(initialQuery)

            binding.btnBack.setOnClickListener {
                onBackClick()
            }

            binding.etSearchQuery.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = binding.etSearchQuery.text?.toString().orEmpty()
                    if (query.isNotBlank()) {
                        viewModel.search(query)
                    } else {
                        viewModel.clearSearch()
                    }
                    true
                } else {
                    false
                }
            }

            binding.btnRetry.setOnClickListener {
                val query = binding.etSearchQuery.text?.toString().orEmpty()
                viewModel.search(query)
            }

            binding.root.tag = binding
            binding.root
        },
        update = { view ->
            val binding = view.tag as? ScreenSearchBinding ?: return@AndroidView
            val productAdapter = binding.rvSearchProducts.adapter as? ProductAdapter

            when (val state = uiState) {
                is SearchUiState.Initial -> {
                    binding.progressBar.isVisible = false
                    binding.tvResultCount.isVisible = false
                    binding.rvSearchProducts.isVisible = false
                    binding.layoutError.isVisible = false
                    binding.tvEmptyMessage.isVisible = true
                    binding.tvEmptyMessage.text = "Type product title, description, or category to search"
                }
                is SearchUiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.tvResultCount.isVisible = false
                    binding.rvSearchProducts.isVisible = false
                    binding.layoutError.isVisible = false
                    binding.tvEmptyMessage.isVisible = false
                }
                is SearchUiState.Empty -> {
                    binding.progressBar.isVisible = false
                    binding.tvResultCount.isVisible = false
                    binding.rvSearchProducts.isVisible = false
                    binding.layoutError.isVisible = false
                    binding.tvEmptyMessage.isVisible = true
                    val currentQuery = binding.etSearchQuery.text?.toString().orEmpty()
                    binding.tvEmptyMessage.text = "No matching products found for '$currentQuery'"
                }
                is SearchUiState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.tvResultCount.isVisible = false
                    binding.rvSearchProducts.isVisible = false
                    binding.layoutError.isVisible = true
                    binding.tvEmptyMessage.isVisible = false
                    binding.tvErrorMessage.text = state.message
                }
                is SearchUiState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.tvResultCount.isVisible = true
                    binding.rvSearchProducts.isVisible = true
                    binding.layoutError.isVisible = false
                    binding.tvEmptyMessage.isVisible = false

                    binding.tvResultCount.text = "Found ${state.products.size} results for '${state.query}'"
                    productAdapter?.submitList(state.products)
                }
            }
        }
    )
}
