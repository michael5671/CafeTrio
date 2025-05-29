package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class OrderHistoryItem(
    val id: String,
    val date: String,
    val items: List<String>,
    val status: OrderStatus
)

enum class OrderStatus {
    DELIVERING,
    DELIVERED,
    CANCELLED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F4E1)
    
    // Sample data
    val orderHistory = listOf(
        OrderHistoryItem(
            "1",
            "10/04",
            listOf("1 Smoothie Xoài Nhiệt Đới Granola"),
            OrderStatus.DELIVERING
        ),
        OrderHistoryItem(
            "2",
            "09/04",
            listOf("1 Smoothie Xoài Nhiệt Đới Granola"),
            OrderStatus.DELIVERED
        ),
        OrderHistoryItem(
            "3",
            "08/04",
            listOf("1 Smoothie Xoài Nhiệt Đới Granola"),
            OrderStatus.CANCELLED
        ),
        OrderHistoryItem(
            "4",
            "07/04",
            listOf("1 Smoothie Xoài Nhiệt Đới Granola"),
            OrderStatus.DELIVERED
        ),
        OrderHistoryItem(
            "5",
            "06/04",
            listOf("1 Smoothie Xoài Nhiệt Đới Granola"),
            OrderStatus.DELIVERED
        ),
        OrderHistoryItem(
            "6",
            "05/04",
            listOf("1 Smoothie Xoài Nhiệt Đới Granola"),
            OrderStatus.CANCELLED
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lịch sử đơn hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF553311)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF553311)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            items(orderHistory) { order ->
                OrderHistoryCard(order = order)
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: OrderHistoryItem) {
    val statusColor = when (order.status) {
        OrderStatus.DELIVERED -> Color(0xFF66BB6A)
        OrderStatus.CANCELLED -> Color(0xFFDE3F3F)
        OrderStatus.DELIVERING -> Color(0xFF553311)
    }
    
    val statusText = when (order.status) {
        OrderStatus.DELIVERED -> "Giao hàng thành công"
        OrderStatus.CANCELLED -> "Đơn hàng đã hủy"
        OrderStatus.DELIVERING -> "Đang giao hàng"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shipper icon
            Image(
                painter = painterResource(id = R.drawable.ic_shipper),
                contentDescription = "Shipper",
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Order details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Giao hàng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF553311)
                )
                
                Text(
                    text = "Nguyễn Đình Tuấn",
                    fontSize = 14.sp,
                    color = Color(0xFF553311)
                )
                
                Text(
                    text = "0981234567",
                    fontSize = 14.sp,
                    color = Color(0xFF553311)
                )
                
                order.items.forEach { item ->
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        color = Color(0xFF553311)
                    )
                }
            }
            
            // Date and status
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = order.date,
                    fontSize = 14.sp,
                    color = Color(0xFF553311)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = statusText,
                    fontSize = 14.sp,
                    color = statusColor
                )
            }
        }
    }
} 