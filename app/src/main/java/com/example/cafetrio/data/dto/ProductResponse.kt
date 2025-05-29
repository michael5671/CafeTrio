package com.example.cafetrio.data.dto

data class ProductResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val categoryId: Long,
    val categoryName: String
)
