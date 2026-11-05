package com.example.catalogone.domain.repository

import com.example.catalogone.domain.model.Product

interface ProductRepository {
    suspend fun getAllProducts(): Result<List<Product>>
    suspend fun getFeaturedProducts(): Result<List<Product>>
    suspend fun getCategories(): Result<List<String>>
    suspend fun getProductsByCategory(category: String): Result<List<Product>>
    suspend fun searchProducts(query: String): Result<List<Product>>
    suspend fun getProductDetails(id: Int): Result<Product>
}
