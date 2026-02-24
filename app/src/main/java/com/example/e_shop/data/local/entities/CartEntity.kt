package com.example.e_shop.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_shop.data.model.CartItem
import com.example.e_shop.data.model.Product
import com.example.e_shop.data.model.Category

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val productId: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val image: String,
    val categoryId: Int,
    val categoryName: String,
    val categoryImage: String,
    val description: String
) {
    fun toCartItem(): CartItem {
        return CartItem(
            product = Product(
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
            ),
            quantity = quantity
        )
    }

    companion object {
        fun fromCartItem(item: CartItem): CartEntity {
            return CartEntity(
                productId = item.product.id ?: 0,
                title = item.product.safeTitle,
                price = item.product.price ?: 0.0,
                quantity = item.quantity,
                image = item.product.firstImage,
                categoryId = item.product.category?.id ?: 0,
                categoryName = item.product.safeCategory.safeName,
                categoryImage = item.product.safeCategory.safeImage,
                description = item.product.safeDescription
            )
        }
    }
}
