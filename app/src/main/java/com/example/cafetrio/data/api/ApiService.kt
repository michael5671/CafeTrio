package com.example.cafetrio.data.api


import com.example.cafetrio.data.dto.CategoryResponse
import com.example.cafetrio.data.dto.LoginRequest
import com.example.cafetrio.data.dto.LoginResponse
import com.example.cafetrio.data.dto.ProductCreateRequest
import com.example.cafetrio.data.dto.ProductDetailResponse
import com.example.cafetrio.data.dto.ProductResponse
import com.example.cafetrio.data.dto.RegisterRequest
import com.example.cafetrio.data.dto.ResendOtpRequest
import com.example.cafetrio.data.dto.ResetPasswordRequest
import com.example.cafetrio.data.dto.VerifyOtpRequest
import com.example.cafetrio.data.dto.CategoryRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.PUT


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

    // Lấy danh sách sản phẩm must-try
    @GET("api/products/must-try")
    fun getMustTryProducts(): Call<List<ProductResponse>>

    @GET("api/products/id/{productId}")
    fun getProductDetail(@Path("productId") productId: String): Call<ProductDetailResponse>

    @POST("api/products")
    fun createProduct(@Body request: ProductCreateRequest): Call<ProductDetailResponse>

    @GET("api/categories")
    fun getCategories(): Call<ResponseBody>

    @GET("api/products")
    fun getProducts(
        @Query("params") params: String,
        @Query("pageable") pageable: String
    ): Call<ResponseBody>

    @DELETE("api/products/id/{productId}")
    fun deleteProduct(@Path("productId") productId: String): Call<ResponseBody>

    @PUT("api/products/id/{productId}")
    fun updateProduct(@Path("productId") productId: String, @Body request: ProductCreateRequest): Call<ProductDetailResponse>


    @POST("api/categories")
    fun createCategory(@Body request: CategoryRequest): Call<CategoryResponse>

    @GET("api/categories/{id}")
    fun getCategoryDetail(@Path("id") categoryId: String): Call<CategoryResponse>

    @PUT("api/categories/{id}")
    fun updateCategory(@Path("id") categoryId: String, @Body request: CategoryRequest): Call<CategoryResponse>

    @DELETE("api/categories/{id}")
    fun deleteCategory(@Path("id") categoryId: String): Call<ResponseBody> // Assuming simple success/error response
}
