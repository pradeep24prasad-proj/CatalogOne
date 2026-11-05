package com.example.catalogone.data.remote

import com.example.catalogone.data.remote.dto.EnquiryRequestDto
import com.example.catalogone.data.remote.dto.EnquiryResponseDto
import com.example.catalogone.data.remote.dto.ProductDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApiService {

    @GET("api/products")
    suspend fun getAllProducts(): List<ProductDto>

    @GET("api/products/featured")
    suspend fun getFeaturedProducts(): List<ProductDto>

    @GET("api/categories")
    suspend fun getCategories(): List<String>

    @GET("api/categories/{category}/products")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): List<ProductDto>

    @GET("api/products")
    suspend fun searchProducts(
        @Query("search") query: String
    ): List<ProductDto>

    @GET("api/products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: Int
    ): ProductDto

    @POST("api/enquiries")
    suspend fun submitEnquiry(
        @Body enquiry: EnquiryRequestDto
    ): EnquiryResponseDto
}
