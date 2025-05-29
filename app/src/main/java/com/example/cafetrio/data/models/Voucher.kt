package com.example.cafetrio.data.models

data class Voucher(
    val id: String,
    val title: String,
    val type: String, // "PICKUP" or "DELIVERY"
    val discount: String,
    val expiry: String
) 