package com.example.e_shop.data.repository

import com.example.e_shop.data.local.dao.CartDao
import com.example.e_shop.data.local.entities.CartEntity
import com.example.e_shop.data.model.CartItem
import com.example.e_shop.data.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    val cartItems: Flow<List<CartItem>> = cartDao.getAllCartItems().map { entities ->
        entities.map { it.toCartItem() }
    }

    fun addToCart(product: Product) {
        scope.launch {
            val existingEntity = cartDao.getCartItem(product.id ?: 0)
            if (existingEntity != null) {
                cartDao.updateCartItem(existingEntity.copy(quantity = existingEntity.quantity + 1))
            } else {
                cartDao.insertCartItem(CartEntity.fromCartItem(CartItem(product, 1)))
            }
        }
    }

    fun removeFromCart(productId: Int) {
        scope.launch {
            cartDao.deleteCartItem(productId)
        }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        scope.launch {
            if (quantity <= 0) {
                cartDao.deleteCartItem(productId)
                return@launch
            }
            val existingEntity = cartDao.getCartItem(productId)
            if (existingEntity != null) {
                cartDao.updateCartItem(existingEntity.copy(quantity = quantity))
            }
        }
    }

    fun clearCart() {
        scope.launch {
            cartDao.clearCart()
        }
    }
}
