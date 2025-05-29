package com.example.cafetrio.data.dto

data class LoginResponse(
    val token: String,
    val userId: String,
    val fullName: String,
    val email: String
) 