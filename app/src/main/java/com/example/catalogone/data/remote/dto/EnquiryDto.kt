package com.example.catalogone.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EnquiryRequestDto(
    val productId: Int,
    val productTitle: String,
    val customerName: String,
    val email: String,
    val phone: String,
    val message: String
)

@Serializable
data class EnquiryResponseDto(
    val success: Boolean,
    val message: String,
    val enquiryId: String? = null
)
