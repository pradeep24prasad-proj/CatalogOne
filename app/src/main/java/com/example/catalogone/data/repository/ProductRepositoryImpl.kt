package com.example.catalogone.data.repository

import com.example.catalogone.data.mapper.toDomain
import com.example.catalogone.data.remote.CatalogApiService
import com.example.catalogone.domain.model.Product
import com.example.catalogone.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: CatalogApiService
) : ProductRepository {

    override suspend fun getAllProducts(): Result<List<Product>> {
        return runCatching {
            val directResult = runCatching { apiService.getAllProducts().map { it.toDomain() } }.getOrNull()
            if (!directResult.isNullOrEmpty()) {
                directResult
            } else {
                val categories = apiService.getCategories()
                val allProducts = mutableListOf<Product>()
                for (cat in categories) {
                    val products = apiService.getProductsByCategory(cat).map { it.toDomain() }
                    allProducts.addAll(products)
                }
                allProducts.distinctBy { it.id }
            }
        }
    }

    override suspend fun getFeaturedProducts(): Result<List<Product>> {
        return runCatching {
            apiService.getFeaturedProducts().map { it.toDomain() }
        }
    }

    override suspend fun getCategories(): Result<List<String>> {
        return runCatching {
            apiService.getCategories()
        }
    }

    override suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return runCatching {
            apiService.getProductsByCategory(category).map { it.toDomain() }
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return runCatching {
            apiService.searchProducts(query).map { it.toDomain() }
        }
    }

    override suspend fun getProductDetails(id: Int): Result<Product> {
        return runCatching {
            apiService.getProductDetails(id).toDomain()
        }
    }
}
