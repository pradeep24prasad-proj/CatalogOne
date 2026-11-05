package com.example.catalogone.domain.model

data class Enquiry(
    val productId: Int,
    val productTitle: String,
    val customerName: String,
    val email: String,
    val phone: String,
    val message: String
)

data class EnquiryResult(
    val success: Boolean,
    val message: String,
    val enquiryId: String? = null
)
