package com.example.e_shop.data.repository

import com.example.e_shop.data.local.dao.WishlistDao
import com.example.e_shop.data.local.entities.WishlistEntity
import com.example.e_shop.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepository @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    val wishlistItems: Flow<List<Product>> = wishlistDao.getAllWishlistItems().map { entities ->
        entities.map { it.toProduct() }
    }

    suspend fun addToWishlist(product: Product) {
        wishlistDao.addToWishlist(WishlistEntity.fromProduct(product))
    }

    suspend fun removeFromWishlist(productId: Int) {
        wishlistDao.removeFromWishlist(productId)
    }

    fun isProductInWishlist(productId: Int): Flow<Boolean> {
        return wishlistDao.isProductInWishlist(productId)
    }
}
