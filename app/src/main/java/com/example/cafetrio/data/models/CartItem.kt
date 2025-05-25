package com.example.cafetrio.data.models

data class CartItem(
    val id: String,
    val productName: String,
    val size: String,
    val quantity: Int,
    val price: Int,
    val toppings: List<String>,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
) 