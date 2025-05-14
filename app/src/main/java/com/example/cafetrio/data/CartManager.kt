package com.example.cafetrio.data

import com.example.cafetrio.data.models.CartItem
import com.example.cafetrio.data.models.Order
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class CartManager private constructor() {
    private val currentItems: SnapshotStateList<CartItem> = mutableStateListOf()
    private val orders: SnapshotStateList<Order> = mutableStateListOf()
    private var nextOrderId = 1

    fun addToCart(item: CartItem) {
        // Thêm vào giỏ hàng hiện tại
        currentItems.add(item)
        
        // Tạo đơn hàng mới từ sản phẩm
        val newOrder = Order(
            id = nextOrderId++,
            customerName = "Nguyễn Đình Tuấn",
            phoneNumber = "0981234567",
            items = listOf(item),
            totalAmount = item.price * item.quantity
        )
        
        // Thêm đơn hàng mới vào danh sách
        orders.add(newOrder)
    }

    fun getCartItems(): List<CartItem> {
        return currentItems.toList()
    }
    
    fun getOrders(): List<Order> {
        return orders.toList()
    }
    
    fun getOrderCount(): Int {
        return orders.size
    }
    
    fun clearCart() {
        currentItems.clear()
    }
    
    fun clearAllOrders() {
        orders.clear()
        nextOrderId = 1
    }
    
    companion object {
        @Volatile
        private var instance: CartManager? = null
        
        fun getInstance(): CartManager {
            return instance ?: synchronized(this) {
                instance ?: CartManager().also { instance = it }
            }
        }
    }
} 