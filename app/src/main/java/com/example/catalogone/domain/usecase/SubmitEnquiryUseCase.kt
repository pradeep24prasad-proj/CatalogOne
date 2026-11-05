package com.example.catalogone.domain.usecase

import com.example.catalogone.domain.model.Enquiry
import com.example.catalogone.domain.model.EnquiryResult
import com.example.catalogone.domain.repository.EnquiryRepository
import javax.inject.Inject

class SubmitEnquiryUseCase @Inject constructor(
    private val repository: EnquiryRepository
) {
    suspend operator fun invoke(enquiry: Enquiry): Result<EnquiryResult> {
        val validationError = validate(enquiry)
        if (validationError != null) {
            return Result.failure(IllegalArgumentException(validationError))
        }
        return repository.submitEnquiry(enquiry)
    }

    fun validate(enquiry: Enquiry): String? {
        if (enquiry.customerName.isBlank()) {
            return "Customer name is required"
        }
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (enquiry.email.isBlank() || !emailRegex.matches(enquiry.email.trim())) {
            return "Valid email address is required"
        }
        if (enquiry.phone.isBlank() || enquiry.phone.trim().length < 5) {
            return "Phone number must be at least 5 characters"
        }
        if (enquiry.message.isBlank() || enquiry.message.trim().length < 5) {
            return "Enquiry message must be at least 5 characters"
        }
        return null
    }
}
