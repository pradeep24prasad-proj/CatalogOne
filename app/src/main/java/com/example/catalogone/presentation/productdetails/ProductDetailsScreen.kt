package com.example.catalogone.presentation.productdetails

import android.view.LayoutInflater
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import coil.load
import com.example.catalogone.databinding.ScreenProductDetailsBinding
import java.util.Locale

@Composable
fun ProductDetailsScreen(
    productId: Int,
    viewModel: ProductDetailsViewModel,
    onBackClick: () -> Unit,
    onEnquireClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(productId) {
        viewModel.loadProductDetails(productId)
    }

    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        modifier = modifier.statusBarsPadding(),
        factory = { context ->
            val binding = ScreenProductDetailsBinding.inflate(LayoutInflater.from(context))

            binding.btnBack.setOnClickListener {
                onBackClick()
            }

            binding.btnEnquireNow.setOnClickListener {
                onEnquireClick(productId)
            }

            binding.btnRetry.setOnClickListener {
                viewModel.loadProductDetails(productId)
            }

            binding.root.tag = binding
            binding.root
        },
        update = { view ->
            val binding = view.tag as? ScreenProductDetailsBinding ?: return@AndroidView

            when (val state = uiState) {
                is ProductDetailsUiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.scrollViewContent.isVisible = false
                    binding.cardBottomBar.isVisible = false
                    binding.layoutError.isVisible = false
                }
                is ProductDetailsUiState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.scrollViewContent.isVisible = false
                    binding.cardBottomBar.isVisible = false
                    binding.layoutError.isVisible = true
                    binding.tvErrorMessage.text = state.message
                }
                is ProductDetailsUiState.Success -> {
                    val product = state.product

                    binding.progressBar.isVisible = false
                    binding.scrollViewContent.isVisible = true
                    binding.cardBottomBar.isVisible = true
                    binding.layoutError.isVisible = false

                    binding.ivProductImage.load(product.image)
                    binding.tvCategoryBadge.text = product.category.uppercase(Locale.ROOT)
                    binding.tvProductTitle.text = product.title
                    binding.tvProductPrice.text = "$${String.format(Locale.US, "%.2f", product.price)}"
                    binding.tvProductRating.text = "★ ${product.rating.rate} (${product.rating.count})"
                    binding.tvProductDescription.text = product.description
                }
            }
        }
    )
}
