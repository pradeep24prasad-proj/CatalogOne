package com.example.catalogone.data.mapper

import com.example.catalogone.data.remote.dto.EnquiryRequestDto
import com.example.catalogone.data.remote.dto.EnquiryResponseDto
import com.example.catalogone.data.remote.dto.ProductDto
import com.example.catalogone.data.remote.dto.RatingDto
import com.example.catalogone.domain.model.Enquiry
import com.example.catalogone.domain.model.EnquiryResult
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.model.Rating

fun RatingDto.toDomain(): Rating {
    return Rating(
        rate = rate,
        count = count
    )
}

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating.toDomain()
    )
}

fun Enquiry.toDto(): EnquiryRequestDto {
    return EnquiryRequestDto(
        productId = productId,
        productTitle = productTitle,
        customerName = customerName,
        email = email,
        phone = phone,
        message = message
    )
}

fun EnquiryResponseDto.toDomain(): EnquiryResult {
    return EnquiryResult(
        success = success,
        message = message,
        enquiryId = enquiryId
    )
}
