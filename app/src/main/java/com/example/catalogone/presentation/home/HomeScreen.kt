package com.example.catalogone.presentation.home

import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.example.catalogone.databinding.ScreenHomeBinding
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating
import com.example.catalogone.presentation.components.CategoryAdapter
import com.example.catalogone.presentation.components.ProductAdapter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCategorySelect: (String) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        modifier = modifier.systemBarsPadding(),
        factory = { context ->
            val binding = ScreenHomeBinding.inflate(LayoutInflater.from(context))

            lateinit var categoryAdapter: CategoryAdapter

            categoryAdapter = CategoryAdapter { category ->
                categoryAdapter.setSelectedCategory(category)
                onCategorySelect(category)
            }
            val productAdapter = ProductAdapter(
                onProductClick = { productId ->
                    onProductClick(productId)
                },
                onBrowseAllClick = {
                    onCategorySelect("all")
                }
            )

            binding.rvCategories.adapter = categoryAdapter
            binding.rvFeaturedProducts.adapter = productAdapter

            binding.etSearchQuery.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = binding.etSearchQuery.text?.toString().orEmpty()
                    if (query.isNotBlank()) {
                        onSearchSubmit(query)
                    }
                    true
                } else {
                    false
                }
            }

            binding.btnRetry.setOnClickListener {
                viewModel.loadHomeData()
            }

            binding.root.tag = binding
            binding.root
        },
        update = { view ->
            val binding = view.tag as? ScreenHomeBinding ?: return@AndroidView
            val categoryAdapter = binding.rvCategories.adapter as? CategoryAdapter
            val productAdapter = binding.rvFeaturedProducts.adapter as? ProductAdapter

            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.scrollViewContent.isVisible = false
                    binding.layoutError.isVisible = false
                }
                is HomeUiState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.scrollViewContent.isVisible = false
                    binding.layoutError.isVisible = true
                    binding.tvErrorMessage.text = state.message
                }
                is HomeUiState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.scrollViewContent.isVisible = true
                    binding.layoutError.isVisible = false

                    val categoriesWithAll = listOf("all") + state.categories.filter { it.lowercase() != "all" }
                    categoryAdapter?.submitList(categoriesWithAll)

                    binding.tvFeaturedBadge.text = view.context.getString(com.example.catalogone.R.string.featured_badge, state.featuredProducts.size)

                    val browseAllDummyProduct = Product(
                        id = -1,
                        title = "Browse All",
                        price = 0.0,
                        description = "",
                        category = "all",
                        image = "",
                        rating = Rating(0.0, 0)
                    )
                    val productsWithBrowseAll = state.featuredProducts.take(5) + browseAllDummyProduct
                    productAdapter?.submitList(productsWithBrowseAll)
                }
            }
        }
    )
}
