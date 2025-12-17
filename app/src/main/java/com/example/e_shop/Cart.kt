package com.example.e_shop

object Cart {
    val products = mutableListOf<String>()

    fun addProduct(product: String) {
        products.add(product)
    }
}