package com.example.cafetrio.data.api

import android.util.Log
import com.example.cafetrio.data.dto.OrderResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val apiService: ApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    private val gson: Gson by lazy {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Giả định UTC nếu không có time zone
        GsonBuilder()
            .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(
                    json: JsonElement?,
                    typeOfT: Type?,
                    context: JsonDeserializationContext?
                ): Date? {
                    return json?.asString?.let { dateStr ->
                        try {
                            dateFormat.parse(dateStr)
                        } catch (e: Exception) {
                            Log.e("ApiClient", "Date parsing error: $dateStr", e)
                            null
                        }
                    }
                }
            })
            .create()
    }
}