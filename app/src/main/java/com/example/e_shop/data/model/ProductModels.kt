package com.example.e_shop.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int? = null,
    val name: String? = null,
    val image: String? = null
) {
    val safeName: String
        get() = name ?: "Unknown Category"

    val safeImage: String
        get() = image ?: ""
}

@Serializable
data class Product(
    val id: Int? = null,
    val title: String? = null,
    val price: Double? = null,
    val description: String? = null,
    val images: List<String>? = null,
    val category: Category? = null
) {
    val safeTitle: String
        get() = title ?: "Untitled Product"

    val safePrice: Double
        get() = price ?: 0.0

    val safeDescription: String
        get() = description ?: "No description available."

    val safeImages: List<String>
        get() = images ?: emptyList()

    val firstImage: String
        get() = safeImages.firstOrNull() ?: ""
        
    val safeCategory: Category
        get() = category ?: Category(name = "Uncategorized")
}
