package com.example.catalogone.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    val rate: Double = 0.0,
    val count: Int = 0
)
