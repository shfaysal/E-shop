package com.example.e_shop.data.repository

import com.example.e_shop.data.model.Category
import com.example.e_shop.data.model.Product
import com.example.e_shop.data.network.ApiService
import com.example.e_shop.util.SafeResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProducts(): SafeResult<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                SafeResult.Success(response.body() ?: emptyList())
            } else {
                SafeResult.Error(response.message())
            }
        } catch (e: Exception) {
            SafeResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getProductsByCategory(categoryId: Int): SafeResult<List<Product>> {
        return try {
            val response = apiService.getProductsByCategory(categoryId)
            if (response.isSuccessful) {
                SafeResult.Success(response.body() ?: emptyList())
            } else {
                SafeResult.Error(response.message())
            }
        } catch (e: Exception) {
            SafeResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getProduct(id: Int): SafeResult<Product> {
        return try {
            val response = apiService.getProduct(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    SafeResult.Success(it)
                } ?: SafeResult.Error("Product not found")
            } else {
                SafeResult.Error(response.message())
            }
        } catch (e: Exception) {
            SafeResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getRelatedProducts(id: Int): SafeResult<List<Product>> {
        return try {
            // Try specific endpoint first
            val response = apiService.getRelatedProducts(id)
            if (response.isSuccessful) {
                SafeResult.Success(response.body() ?: emptyList())
            } else {
                // Fallback logic if needed could go here, but let's stick to the API contract
                SafeResult.Error(response.message())
            }
        } catch (e: Exception) {
            SafeResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getCategories(): SafeResult<List<Category>> {
        return try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                SafeResult.Success(response.body() ?: emptyList())
            } else {
                SafeResult.Error(response.message())
            }
        } catch (e: Exception) {
            SafeResult.Error(e.message ?: "Unknown error")
        }
    }
}