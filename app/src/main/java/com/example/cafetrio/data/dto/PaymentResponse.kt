package com.example.cafetrio.data.dto

data class PaymentResponse(
    val code: String,
    val message: String,
    val paymentUrl: String,
    val amount: Int
)

data class PaymentRequest(
    val amount: Int,
    val orderId: String,
    val language: String = "vn"
)