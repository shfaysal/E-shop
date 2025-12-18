package com.example.e_shop.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null
) {
    val safeEmail: String get() = email ?: ""
    val safePassword: String get() = password ?: ""
}

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null
) {
    val safeAccessToken: String get() = accessToken ?: ""
    val safeRefreshToken: String get() = refreshToken ?: ""
}

data class UserRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("avatar") val avatar: String? = null
) {
    val safeName: String get() = name ?: ""
    val safeEmail: String get() = email ?: ""
    val safePassword: String get() = password ?: ""
    val safeAvatar: String get() = avatar ?: ""
}

data class UserResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("avatar") val avatar: String? = null
) {
    val safeId: Int get() = id ?: 0
    val safeEmail: String get() = email ?: ""
    val safePassword: String get() = password ?: ""
    val safeName: String get() = name ?: ""
    val safeRole: String get() = role ?: ""
    val safeAvatar: String get() = avatar ?: ""
}

data class FileUploadResponse(
    @SerializedName("originalname") val originalName: String? = null,
    @SerializedName("filename") val fileName: String? = null,
    @SerializedName("location") val location: String? = null
) {
    val safeOriginalName: String get() = originalName ?: ""
    val safeFileName: String get() = fileName ?: ""
    val safeLocation: String get() = location ?: ""
}
