package com.example.catalogone.presentation.enquiry

import android.view.LayoutInflater
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.catalogone.databinding.ScreenThankYouBinding

@Composable
fun ThankYouScreen(
    productTitle: String,
    customerName: String,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.systemBarsPadding(),
        factory = { context ->
            val binding = ScreenThankYouBinding.inflate(LayoutInflater.from(context))

            binding.tvThankYouHeader.text = "Thank You, $customerName!"
            binding.tvThankYouMessage.text = "Your enquiry for '$productTitle' has been submitted successfully.\n\nOur team has dispatched a notification email and will contact you shortly."

            binding.btnBackToHome.setOnClickListener {
                onHomeClick()
            }

            binding.root.tag = binding
            binding.root
        },
        update = { view ->
            val binding = view.tag as? ScreenThankYouBinding ?: return@AndroidView
            binding.tvThankYouHeader.text = "Thank You, $customerName!"
            binding.tvThankYouMessage.text = "Your enquiry for '$productTitle' has been submitted successfully.\n\nOur team has dispatched a notification email and will contact you shortly."
        }
    )
}
