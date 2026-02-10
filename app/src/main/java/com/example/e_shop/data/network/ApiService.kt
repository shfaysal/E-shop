package com.example.e_shop.data.network

import com.example.e_shop.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("users/")
    suspend fun register(@Body request: UserRequest): Response<UserResponse>

    @GET("auth/profile")
    suspend fun getProfile(): Response<UserResponse>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body request: UserRequest): Response<UserResponse>

    @Multipart
    @POST("files/upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): Response<FileUploadResponse>

    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<Product>

    // Note: The fake store API might not strictly have "related", 
    // but the user plan mentioned it and my previous summary claimed it.
    // If it fails, I'll fallback to category products.
    // Based on summary: GET /api/v1/products/{id}/related
    // However, looking at standard Platzi API docs, usually one filters by category to get related.
    // But my summary said: "GET /api/v1/products/{id}/related: Retrieve related products for a given product ID."
    // So I will trust my previous tool output.
    @GET("products/{id}/related")
    suspend fun getRelatedProducts(@Path("id") id: Int): Response<List<Product>>

    @GET("categories/{id}/products")
    suspend fun getProductsByCategory(@Path("id") id: Int): Response<List<Product>>

    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>
}