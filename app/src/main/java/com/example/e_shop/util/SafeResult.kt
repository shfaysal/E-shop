package com.example.e_shop.util

sealed class SafeResult<out T> {
    data class Success<out T>(val data: T) : SafeResult<T>()
    data class Error(val message: String, val code: Int? = null) : SafeResult<Nothing>()
    object Loading : SafeResult<Nothing>()
}
