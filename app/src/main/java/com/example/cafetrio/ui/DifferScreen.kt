package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifferScreen(
    onBackClick: () -> Unit = {},
    onNavigationItemClick: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F4E1)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Khác",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = CafeBrown
                        )
                    }
                },
                actions = {
                    // Voucher Button with custom shape
                    Box(
                        modifier = Modifier.padding(end = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .width(70.dp)
                                .height(40.dp)
                                .background(
                                    color = Color(0xFFFFFFFF), 
                                    shape = RoundedCornerShape(size = 25.dp)
                                )
                                .clickable { /* TODO: Handle voucher click */ },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_coupon),
                                contentDescription = "Vouchers",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 8.dp)
                            )
                            
                            Text(
                                text = "11", 
                                color = CafeBrown,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                            )
                        }
                    }
                    
                    // Notification button with shadow and circular shape
                    Box(
                        modifier = Modifier.padding(end = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .background(
                                    color = Color(0xFFFFFFFF), 
                                    shape = RoundedCornerShape(size = 45.dp)
                                )
                                .clickable { /* TODO: Handle notification click */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_noti),
                                contentDescription = "Notifications",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CafeBeige
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = backgroundColor,
                contentColor = CafeBrown
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Trang chủ
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("home") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Trang chủ",
                            color = Color(0xFFAF8F6F),
                            fontSize = 12.sp
                        )
                    }
                    
                    // Đặt hàng
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("order") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_booked),
                            contentDescription = "Order",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Đặt hàng",
                            color = Color(0xFFAF8F6F),
                            fontSize = 12.sp
                        )
                    }
                    
                    // Ưu đãi
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("rewards") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_voucher),
                            contentDescription = "Rewards",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Ưu đãi",
                            color = Color(0xFFAF8F6F),
                            fontSize = 12.sp
                        )
                    }
                    
                    // Khác (active)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("more") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_differ),
                            contentDescription = "More",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Khác",
                            color = Color(0xFF543310),
                            fontSize = 12.sp
                        )
                        // Active indicator
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(32.dp)
                                .height(2.dp)
                                .background(Color(0xFF543310))
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            // Tiện ích Section
            Text(
                text = "Tiện ích",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF543310),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Row 1: Lịch sử đơn hàng & Điều khoản
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Lịch sử đơn hàng
                UtilityButton(
                    iconResId = R.drawable.ic_lichsudonhang,
                    title = "Lịch sử đơn hàng",
                    iconTint = Color(0xFFF9A825),
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO */ }
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Điều khoản
                UtilityButton(
                    iconResId = R.drawable.ic_dieukhoan,
                    title = "Điều khoản",
                    iconTint = Color(0xFF8E24AA),
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO */ }
                )
            }
            
            // Row 2: Điều khoản MoMo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                // Điều khoản MoMo
                UtilityButton(
                    iconResId = R.drawable.ic_dieukhoan,
                    title = "Điều khoản MoMo",
                    iconTint = Color(0xFF8E24AA),
                    modifier = Modifier.fillMaxWidth(0.49f),
                    onClick = { /* TODO */ }
                )
            }
            
            // Hỗ trợ Section
            Text(
                text = "Hỗ trợ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF543310),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Support Items
            SupportItem(
                iconResId = R.drawable.ic_danhgiadonhang,
                title = "Đánh giá đơn hàng",
                onClick = { /* TODO */ }
            )
            
            Spacer(modifier = Modifier.height(1.dp))
            
            SupportItem(
                iconResId = R.drawable.ic_lienhe,
                title = "Liên hệ và góp ý",
                onClick = { /* TODO */ }
            )
            
            Spacer(modifier = Modifier.height(1.dp))
            
            SupportItem(
                iconResId = R.drawable.ic_hoadon,
                title = "Hướng dẫn xuất hóa đơn GTGT",
                onClick = { /* TODO */ }
            )
            
            // Tài khoản Section
            Text(
                text = "Tài khoản",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF543310),
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            )
            
            // Account Items
            SupportItem(
                iconResId = R.drawable.ic_ttcanhan,
                title = "Thông tin cá nhân",
                onClick = { /* TODO */ }
            )
            
            Spacer(modifier = Modifier.height(1.dp))
            
            SupportItem(
                iconResId = R.drawable.ic_diachi,
                title = "Địa chỉ đã lưu",
                onClick = { /* TODO */ }
            )
            
            Spacer(modifier = Modifier.height(1.dp))
            
            SupportItem(
                iconResId = R.drawable.ic_caidat,
                title = "Cài đặt",
                onClick = { /* TODO */ }
            )
            
            Spacer(modifier = Modifier.height(1.dp))
            
            SupportItem(
                iconResId = R.drawable.ic_logout,
                title = "Đăng xuất",
                onClick = { onLogoutClick() }
            )
        }
    }
}

@Composable
fun UtilityButton(
    iconResId: Int,
    title: String,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconTint.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFF543310),
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SupportItem(
    iconResId: Int,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color(0xFFFFFFFF))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = title,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Text(
                text = title,
                fontSize = 16.sp,
                color = CafeBrown,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
            
            Box(
                modifier = Modifier.padding(end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Arrow",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
} 