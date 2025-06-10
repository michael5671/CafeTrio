package com.example.cafetrio.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.*
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
import com.example.cafetrio.data.api.ApiClient
import com.example.cafetrio.data.dto.OrderDetail
import com.example.cafetrio.data.dto.OrderResponse
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeTrioTheme
import com.example.cafetrio.utils.FormatUtils
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    onNavigateToPayment: (OrderDetail) -> Unit = {}
) {
    var orders by remember { mutableStateOf<List<OrderDetail>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Gọi API để lấy danh sách đơn hàng
    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        Log.d("CartScreen", "Calling API: http://10.0.2.2:8080/api/order/me")
        ApiClient.apiService.getMyOrders().enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                isLoading = false
                Log.d("CartScreen", "API Response: ${response.code()} - ${response.message()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("CartScreen", "Response Body: $body")
                    if (body != null) {
                        orders = body.data?.content ?: emptyList()
                        Log.d("CartScreen", "Orders size: ${orders.size}, OrderItemList size: ${orders.flatMap { it.orderItemList }.size}")
                    } else {
                        error = "Không nhận được dữ liệu từ server"
                    }
                } else {
                    error = "Không thể tải đơn hàng: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                isLoading = false
                error = "Lỗi kết nối: ${t.message}"
                Log.e("CartScreen", "API Failure: ${t.message}", t)
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Danh sách sản phẩm",
                            color = CafeBrown,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
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
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (error != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = error ?: "Lỗi",
                        color = CafeBrown,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            isLoading = true
                            error = null
                            ApiClient.apiService.getMyOrders().enqueue(object : Callback<OrderResponse> {
                                override fun onResponse(
                                    call: Call<OrderResponse>,
                                    response: Response<OrderResponse>
                                ) {
                                    isLoading = false
                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body != null) {
                                            orders = body.data?.content ?: emptyList()
                                        } else {
                                            error = "Không nhận được dữ liệu từ server"
                                        }
                                    } else {
                                        error = "Không thể tải đơn hàng: ${response.message()}"
                                    }
                                }

                                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                                    isLoading = false

                                    error = when (t) {
                                        is java.net.UnknownHostException -> "Không thể kết nối đến máy chủ. Kiểm tra mạng."
                                        is java.net.SocketTimeoutException -> "Hết thời gian chờ. Vui lòng thử lại."
                                        is javax.net.ssl.SSLHandshakeException -> "Lỗi bảo mật kết nối (SSL)."
                                        else -> "Lỗi kết nối: ${t.localizedMessage}"
                                    }

                                    Log.e("CartScreen", "API Failure", t)
                                }
                            })
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CafeBrown),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Thử lại", color = Color.White, fontSize = 16.sp)
                    }
                }
            } else if (orders.isEmpty() || orders.flatMap { it.orderItemList }.isEmpty()) {
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
                            .size(120.dp)
                            .padding(bottom = 24.dp)
                    )
                    Text(
                        text = "Chưa có sản phẩm nào",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CafeBrown,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Vui lòng thêm sản phẩm để thanh toán!",
                        fontSize = 16.sp,
                        color = CafeBrown.copy(alpha = 0.7f),
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
                    items(orders.flatMap { it.orderItemList }) { orderItem ->
                        OrderItem(
                            orderItem = orderItem,
                            totalPrice = orders.firstOrNull { it.orderItemList.contains(orderItem) }?.totalPrice ?: 0,
                            onClick = {
                                orders.firstOrNull { it.orderItemList.contains(orderItem) }?.let { order ->
                                    onNavigateToPayment(order)
                                }
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItem(
    orderItem: com.example.cafetrio.data.dto.OrderItem,
    totalPrice: Int,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5E8C7))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "Product Icon",
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = orderItem.name ?: "Sản phẩm #${orderItem.id.takeLast(6)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CafeBrown,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Số lượng: ${orderItem.amount}",
                    fontSize = 14.sp,
                    color = CafeBrown.copy(alpha = 0.8f)
                )
            }
            Text(
                text = FormatUtils.formatPrice(orderItem.price?.toInt() ?: 0),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CafeBrown
            )
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