package com.example.e_shop.data.repository

import com.example.e_shop.data.local.TokenManager
import com.example.e_shop.data.model.*
import com.example.e_shop.data.network.ApiService
import com.example.e_shop.util.SafeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    fun login(request: LoginRequest): Flow<SafeResult<String>> = flow {
        emit(SafeResult.Loading)
        try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                val token = response.body()?.accessToken
                if (token != null) {
                    tokenManager.saveToken(token)
                    emit(SafeResult.Success(token))
                } else {
                    emit(SafeResult.Error("Token not found"))
                }
            } else {
                emit(SafeResult.Error("Login failed: ${response.message()}", response.code()))
            }
        } catch (e: Exception) {
            emit(SafeResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun register(request: UserRequest): Flow<SafeResult<Boolean>> = flow {
        emit(SafeResult.Loading)
        try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                emit(SafeResult.Success(true))
            } else {
                emit(SafeResult.Error("Registration failed: ${response.message()}", response.code()))
            }
        } catch (e: Exception) {
            emit(SafeResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun uploadImage(file: MultipartBody.Part): Flow<SafeResult<String>> = flow {
        emit(SafeResult.Loading)
        try {
            val response = apiService.uploadFile(file)
            if (response.isSuccessful) {
                val location = response.body()?.location
                if (location != null) {
                    emit(SafeResult.Success(location))
                } else {
                    emit(SafeResult.Error("Upload failed: Location not found"))
                }
            } else {
                emit(SafeResult.Error("Upload failed: ${response.message()}", response.code()))
            }
        } catch (e: Exception) {
            emit(SafeResult.Error(e.message ?: "Unknown error occurred"))
        }
    }
}