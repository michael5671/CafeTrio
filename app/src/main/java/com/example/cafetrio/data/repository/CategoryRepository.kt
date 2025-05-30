package com.example.cafetrio.data.repository

import com.example.cafetrio.data.api.ApiClient
import com.example.cafetrio.data.dto.CategoryRequest
import com.example.cafetrio.data.dto.CategoryResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import org.json.JSONArray

object CategoryRepository {
    fun getCategories(callback: (List<CategoryResponse>?, Throwable?) -> Unit) {
        ApiClient.apiService.getCategories().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val body = response.body()?.string()
                    try {
                        val json = JSONObject(body)
                        val dataArr = json.getJSONArray("data")
                        val categories = mutableListOf<CategoryResponse>()
                        for (i in 0 until dataArr.length()) {
                            val obj = dataArr.getJSONObject(i)
                            categories.add(
                                CategoryResponse(
                                    id = obj.getInt("id"),
                                    name = obj.getString("name"),
                                    description = obj.optString("description", "")
                                )
                            )
                        }
                        callback(categories, null)
                    } catch (e: Exception) {
                        callback(null, e)
                    }
                } else {
                    callback(null, Exception("Lỗi lấy danh mục: ${response.code()}"))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun createCategory(request: CategoryRequest, callback: (CategoryResponse?, Throwable?) -> Unit) {
        ApiClient.apiService.createCategory(request).enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Exception("Lỗi tạo danh mục: ${response.code()}"))
                }
            }
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun updateCategory(id: String, request: CategoryRequest, callback: (CategoryResponse?, Throwable?) -> Unit) {
        ApiClient.apiService.updateCategory(id, request).enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Exception("Lỗi cập nhật danh mục: ${response.code()}"))
                }
            }
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun deleteCategory(id: String, callback: (Boolean, Throwable?) -> Unit) {
        ApiClient.apiService.deleteCategory(id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                callback(response.isSuccessful, null)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(false, t)
            }
        })
    }
} 