package com.example.cafetrio.data.dto

data class ProductCreateRequest (
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val categoryId: Int
    )