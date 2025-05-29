package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.data.CartManager
import com.example.cafetrio.data.models.Order
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeTrioTheme
import com.example.cafetrio.utils.FormatUtils
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    onOrderClick: (Order) -> Unit = {}
) {
    val cartManager = remember { CartManager.getInstance() }
    val orders = remember { cartManager.getOrders() }
    val orderCount = orders.size
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Đơn hàng ",
                                color = CafeBrown,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            if (orderCount > 0) {
                                Text(
                                    text = "($orderCount)",
                                    color = Color(0xFFDE3F3F),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = CafeBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CafeBeige
                )
            )
        },
        containerColor = CafeBeige
    ) { paddingValues ->
        if (orders.isEmpty()) {
            // Hiển thị thông báo khi không có đơn hàng
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "Empty Cart",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Chưa có đơn hàng nào",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CafeBrown,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Vui lòng chọn thêm sản phẩm vào đơn hàng của bạn",
                    fontSize = 15.sp,
                    color = CafeBrown,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                items(orders) { order ->
                    OrderItem(
                        order = order,
                        onClick = { onOrderClick(order) }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun OrderItem(
    order: Order,
    onClick: () -> Unit = {}
) {
    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    val date = dateFormat.format(Date(order.orderDate))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = CafeBeige
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cart icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFAF3E0))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "Cart Icon",
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Order details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Order title with order number
                Text(
                    text = "Đơn hàng ${order.id}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = CafeBrown
                )
                
                // Customer info
                Text(
                    text = "${order.customerName}",
                    fontSize = 14.sp,
                    color = CafeBrown,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = "${order.phoneNumber}",
                    fontSize = 14.sp,
                    color = CafeBrown
                )
                
                // Product info
                val firstItem = order.items.firstOrNull()
                if (firstItem != null) {
                    Text(
                        text = "${firstItem.quantity} ${firstItem.productName}",
                        fontSize = 14.sp,
                        color = CafeBrown,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Date
                Text(
                    text = date,
                    fontSize = 14.sp,
                    color = CafeBrown,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Price
                Text(
                    text = FormatUtils.formatPrice(order.totalAmount),
                    fontSize = 16.sp,
                    color = CafeBrown,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CafeTrioTheme {
        CartScreen()
    }
} 