package com.example.e_shop.data.repository

import com.example.e_shop.data.model.CartItem
import com.example.e_shop.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.product.id == product.id) {
                        it.copy(quantity = it.quantity + 1)
                    } else {
                        it
                    }
                }
            } else {
                currentItems + CartItem(product = product, quantity = 1)
            }
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.product.id != productId }
        }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.product.id == productId) {
                    it.copy(quantity = quantity)
                } else {
                    it
                }
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
