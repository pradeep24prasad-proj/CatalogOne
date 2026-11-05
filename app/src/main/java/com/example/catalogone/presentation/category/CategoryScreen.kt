package com.example.catalogone.presentation.category

import android.view.LayoutInflater
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.example.catalogone.databinding.ScreenCategoryBinding
import com.example.catalogone.presentation.components.ProductAdapter

@Composable
fun CategoryScreen(
    categoryName: String,
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(categoryName) {
        viewModel.loadCategoryProducts(categoryName)
    }

    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        modifier = modifier.statusBarsPadding(),
        factory = { context ->
            val binding = ScreenCategoryBinding.inflate(LayoutInflater.from(context))

            val productAdapter = ProductAdapter(
                onProductClick = { productId ->
                    onProductClick(productId)
                }
            )
            binding.rvCategoryProducts.adapter = productAdapter

            binding.btnBack.setOnClickListener {
                onBackClick()
            }

            binding.btnRetry.setOnClickListener {
                viewModel.loadCategoryProducts(categoryName)
            }

            binding.root.tag = binding
            binding.root
        },
        update = { view ->
            val binding = view.tag as? ScreenCategoryBinding ?: return@AndroidView
            val productAdapter = binding.rvCategoryProducts.adapter as? ProductAdapter

            binding.tvCategoryTitle.text = if (categoryName.equals("all", ignoreCase = true)) "All Products" else categoryName.replaceFirstChar { it.uppercase() }

            when (val state = uiState) {
                is CategoryUiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.rvCategoryProducts.isVisible = false
                    binding.tvEmptyMessage.isVisible = false
                    binding.layoutError.isVisible = false
                }
                is CategoryUiState.Empty -> {
                    binding.progressBar.isVisible = false
                    binding.rvCategoryProducts.isVisible = false
                    binding.tvEmptyMessage.isVisible = true
                    binding.layoutError.isVisible = false
                    binding.tvEmptyMessage.text = "No products found in category '$categoryName'"
                }
                is CategoryUiState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.rvCategoryProducts.isVisible = false
                    binding.tvEmptyMessage.isVisible = false
                    binding.layoutError.isVisible = true
                    binding.tvErrorMessage.text = state.message
                }
                is CategoryUiState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.rvCategoryProducts.isVisible = true
                    binding.tvEmptyMessage.isVisible = false
                    binding.layoutError.isVisible = false

                    productAdapter?.submitList(state.products)
                }
            }
        }
    )
}
