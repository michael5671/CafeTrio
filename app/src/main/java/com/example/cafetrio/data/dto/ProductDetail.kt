package com.example.cafetrio.data.dto

data class ProductDetail(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val categoryId: Int,
    val categoryName: String,
    val createdAt: String,
    val updatedAt: String
)