package com.example.cafetrio.data.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val fullName: String,
//    val phone: String
)
