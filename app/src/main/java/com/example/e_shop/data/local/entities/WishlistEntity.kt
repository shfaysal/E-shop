package com.example.e_shop.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_shop.data.model.Product
import com.example.e_shop.data.model.Category

@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val categoryId: Int,
    val categoryName: String,
    val categoryImage: String,
    val description: String
) {
    fun toProduct(): Product {
        return Product(
            id = productId,
            title = title,
            price = price,
            description = description,
            images = listOf(image),
            category = Category(
                id = categoryId,
                name = categoryName,
                image = categoryImage
            )
        )
    }

    companion object {
        fun fromProduct(product: Product): WishlistEntity {
            return WishlistEntity(
                productId = product.id ?: 0,
                title = product.safeTitle,
                price = product.price ?: 0.0,
                image = product.firstImage,
                categoryId = product.category?.id ?: 0,
                categoryName = product.safeCategory.safeName,
                categoryImage = product.safeCategory.safeImage,
                description = product.safeDescription
            )
        }
    }
}
