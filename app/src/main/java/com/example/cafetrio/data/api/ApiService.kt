package com.example.cafetrio.data.api


import com.example.cafetrio.data.dto.LoginRequest
import com.example.cafetrio.data.dto.LoginResponse
import com.example.cafetrio.data.dto.RegisterRequest
import com.example.cafetrio.data.dto.ResendOtpRequest
import com.example.cafetrio.data.dto.ResetPasswordRequest
import com.example.cafetrio.data.dto.VerifyOtpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    
    @POST("api/auth/register") // hoặc /api/auth/register tùy backend
    fun register(@Body request: RegisterRequest): Call<Void> // hoặc ApiResponse nếu có message
    
    @GET("api/auth/email-verification/{id}")
    fun verifyOtp(@Path("id") id: String): Call<Void>
    
    @POST("api/auth/resend-otp")
    fun resendOtp(@Body request: ResendOtpRequest): Call<Void>
    
    @GET("api/auth/reset-password/{id}")
    fun forgotPassword(@Path("id") id: String): Call<Void>
    
    // Reset password with new password
    @POST("api/auth/reset-password/{email}")
    fun resetPassword(
        @Path("email") email: String,
        @Body request: ResetPasswordRequest
    ): Call<Void>
    
    // Logout user
    @GET("api/auth/logout")
    fun logout(@Header("Authorization") token: String): Call<Void>
}
