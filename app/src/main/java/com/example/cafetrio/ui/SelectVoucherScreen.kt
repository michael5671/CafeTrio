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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.data.models.Voucher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectVoucherScreen(
    onBackClick: () -> Unit = {},
    onVoucherSelect: (Voucher?) -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F1DF)
    var selectedTab by remember { mutableStateOf(0) }
    
    // Sample vouchers data with corresponding images
    val vouchers = remember {
        listOf(
            Voucher(
                id = "pickup_30",
                title = "Giảm 30% Bánh Khi Mua Nước Size Lớn Nhất",
                type = "PICKUP",
                discount = "30%",
                expiry = "Hết hạn 29/04/2024"
            ),
            Voucher(
                id = "delivery_40",
                title = "Giảm 40% + Freeship Đơn Từ 10 Ly",
                type = "DELIVERY",
                discount = "40%",
                expiry = "Hết hạn 30/04/2024"
            ),
            Voucher(
                id = "delivery_30",
                title = "Giảm 30% + Freeship Đơn Từ 3 Ly",
                type = "DELIVERY",
                discount = "30%",
                expiry = "Hết hạn 30/04/2024"
            )
        )
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nhập mã khuyến mãi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF543310)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color(0xFF543310)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Voucher code input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Nhập mã khuyến mãi") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedBorderColor = Color(0xFFBDBDBD),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                
                Button(
                    onClick = { /* Handle apply voucher */ },
                    modifier = Modifier.height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF74512D)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Áp dụng")
                }
            }

            // Tabs
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TabButton(
                            text = "Voucher của bạn",
                            icon = R.drawable.ic_voucher,
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )
                        if (selectedTab == 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(2.dp)
                                    .background(Color(0xFFFB531C))
                            )
                        }
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TabButton(
                            text = "Đổi BEAN",
                            icon = R.drawable.ic_coffeeseed,
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )
                        if (selectedTab == 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(2.dp)
                                    .background(Color(0xFFFB531C))
                            )
                        }
                    }
                }
            }

            // Content based on selected tab
            when (selectedTab) {
                0 -> {
                    // Vouchers tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(vouchers) { voucher ->
                            VoucherCard(
                                voucher = voucher,
                                image = when (voucher.id) {
                                    "pickup_30" -> R.drawable.vc_1
                                    "delivery_40" -> R.drawable.vc_2
                                    else -> R.drawable.vc_3
                                },
                                onClick = { onVoucherSelect(voucher) }
                            )
                        }
                    }
                }
                1 -> {
                    // Bean exchange tab
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Current bean balance
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.change_bean),
                                    contentDescription = "Bean balance",
                                    modifier = Modifier.size(40.dp)
                                )
                                
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "Số bean hiện tại của bạn",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "0 bean",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF543310)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier.size(
                when (icon) {
                    R.drawable.ic_coffeeseed -> 18.dp
                    else -> 24.dp
                }
            )
        )
        Text(
            text = text,
            color = if (isSelected) Color(0xFFFB531C) else Color(0xFF543310),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun VoucherCard(
    voucher: Voucher,
    image: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Voucher image
            Image(
                painter = painterResource(id = image),
                contentDescription = voucher.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            // Voucher details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = voucher.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF543310)
                )
                
                Text(
                    text = voucher.expiry,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
} 