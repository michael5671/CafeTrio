package com.example.cafetrio.data.dto

data class CreateOrderRequest(
    val address: String = "",
    val fullName: String,
    val itemsPrice: Int,
    val orderItems: List<OrderItemRequest>,
    val paymentType: String = "CASH",
    val phone: String,
    val totalPrice: Int,
    val userId: String
)

data class OrderItemRequest(
    val id: String,
    val name: String,
    val description: String = "",
    val price: Int,
    val imageUrl: String = "",
    val amount: Int = 1
) 