package com.example.catalogone.presentation.enquiry

import android.view.LayoutInflater
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import coil.load
import com.example.catalogone.databinding.ScreenEnquiryBinding
import java.util.Locale

@Composable
fun EnquiryScreen(
    productId: Int,
    viewModel: EnquiryViewModel,
    onBackClick: () -> Unit,
    onSubmitSuccess: (productTitle: String, customerName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is EnquiryUiState.SubmittedSuccess) {
            val successState = uiState as EnquiryUiState.SubmittedSuccess
            onSubmitSuccess(successState.productTitle, successState.customerName)
        }
    }

    AndroidView(
        modifier = modifier.systemBarsPadding(),
        factory = { context ->
            val binding = ScreenEnquiryBinding.inflate(LayoutInflater.from(context))

            binding.btnBack.setOnClickListener {
                onBackClick()
            }

            binding.btnSubmitEnquiry.setOnClickListener {
                viewModel.submitEnquiry(
                    customerName = binding.etCustomerName.text?.toString().orEmpty(),
                    email = binding.etEmail.text?.toString().orEmpty(),
                    phone = binding.etPhone.text?.toString().orEmpty(),
                    message = binding.etMessage.text?.toString().orEmpty()
                )
            }

            binding.btnRetry.setOnClickListener {
                viewModel.loadProduct(productId)
            }

            binding.root.tag = binding
            binding.root
        },
        update = { view ->
            val binding = view.tag as? ScreenEnquiryBinding ?: return@AndroidView

            when (val state = uiState) {
                is EnquiryUiState.LoadingProduct -> {
                    binding.progressBar.isVisible = true
                    binding.scrollViewContent.isVisible = false
                    binding.layoutError.isVisible = false
                }
                is EnquiryUiState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.scrollViewContent.isVisible = false
                    binding.layoutError.isVisible = true
                    binding.tvErrorMessage.text = state.message
                }
                is EnquiryUiState.Content -> {
                    binding.progressBar.isVisible = false
                    binding.scrollViewContent.isVisible = true
                    binding.layoutError.isVisible = false

                    binding.ivProductImage.load(state.product.image)
                    binding.tvProductTitle.text = state.product.title
                    binding.tvProductPrice.text = "$${String.format(Locale.US, "%.2f", state.product.price)}"

                    if (state.validationError != null) {
                        binding.tvValidationError.isVisible = true
                        binding.tvValidationError.text = state.validationError
                    } else if (state.errorMessage != null) {
                        binding.tvValidationError.isVisible = true
                        binding.tvValidationError.text = state.errorMessage
                    } else {
                        binding.tvValidationError.isVisible = false
                    }

                    binding.btnSubmitEnquiry.isEnabled = !state.isSubmitting
                    binding.progressBarSubmitting.isVisible = state.isSubmitting
                }
                is EnquiryUiState.SubmittedSuccess -> {
                    // Navigation handled via LaunchedEffect above
                }
            }
        }
    )
}
