package com.example.e_shop.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_shop.data.model.CartItem
import com.example.e_shop.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems

    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.totalPrice }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    fun increaseQuantity(item: CartItem) {
        cartRepository.updateQuantity(item.product.id ?: return, item.quantity + 1)
    }

    fun decreaseQuantity(item: CartItem) {
        cartRepository.updateQuantity(item.product.id ?: return, item.quantity - 1)
    }

    fun removeItem(item: CartItem) {
        cartRepository.removeFromCart(item.product.id ?: return)
    }
    
    fun clearCart() {
        cartRepository.clearCart()
    }
}
