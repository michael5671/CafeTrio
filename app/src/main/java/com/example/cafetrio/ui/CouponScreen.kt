package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponScreen(
    onBackClick: () -> Unit = {},
    onNavigationItemClick: (String) -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F4E1)
    val userName = "NGUYEN DINH TUAN"
    val beanCount = 0
    
    Scaffold(
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
                    
                    // Ưu đãi (active)
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
                    
                    // Khác
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("differ") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_differ),
                            contentDescription = "More",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Khác",
                            color = Color(0xFFAF8F6F),
                            fontSize = 12.sp
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
                .verticalScroll(rememberScrollState())
        ) {
            // Outer frame with brown gradient background - full width with no rounded corners
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFAF8F6F), // 0% - Light brown
                                Color(0xFF74512D)   // 100% - Dark brown
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // User info section with greeting - now inside the brown frame
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Chào bạn,",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = userName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$beanCount BEAN",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Inner membership card box with orange gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFBB063), // Light orange
                                        Color(0xFFC05D0D)  // Dark orange
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Café Trio",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(R.font.agbalumo_regular)),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "BRONZE CLASS",
                                color = Color(0xFF553311),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "MEMBERSHIP CARD",
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Bean info and voucher button - now inside the brown frame
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Còn 100 BEAN nữa bạn sẽ thăng hạng.\nĐổi quà không ảnh hưởng tới việc thăng hạng \ncủa bạn",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Voucher button
                        Button(
                            onClick = { /* TODO: Handle voucher click */ },
                            modifier = Modifier.wrapContentSize(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF8F4E1)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_voucher),
                                    contentDescription = "Vouchers",
                                    modifier = Modifier.size(16.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(2.dp))
                                
                                Text(
                                    text = "Voucher của tôi",
                                    color = Color(0xFF553311),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            
            // Function buttons in a grid layout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // First row with two buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FunctionButton(
                        icon = R.drawable.ic_crown,
                        title = "Hạng thành viên",
                        onClick = { /* TODO: Handle function click */ },
                        iconColor = Color(0xFFFF9800), // Orange
                        modifier = Modifier.weight(1f)
                    )
                    
                    FunctionButton(
                        icon = R.drawable.ic_gift,
                        title = "Hạng thành viên",
                        onClick = { /* TODO: Handle function click */ },
                        iconColor = Color(0xFFFF5722), // Red-orange
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Second row with two buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FunctionButton(
                        icon = R.drawable.ic_coffeeseed,
                        title = "Hạng thành viên",
                        onClick = { /* TODO: Handle function click */ },
                        iconColor = Color(0xFFAF8F6F), // Brown
                        modifier = Modifier.weight(1f)
                    )
                    
                    FunctionButton(
                        icon = R.drawable.ic_person,
                        title = "Hạng thành viên",
                        onClick = { /* TODO: Handle function click */ },
                        iconColor = Color(0xFF2196F3), // Blue
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Voucher section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header with "View all" button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Voucher của bạn",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311)
                    )
                    
                    // "Xem tất cả" without frame, just text
                    Text(
                        text = "Xem tất cả",
                        fontSize = 14.sp,
                        color = Color(0xFFDA7B16),
                        modifier = Modifier.clickable { /* TODO: View all vouchers */ }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Voucher 1 - 30% OFF
                VoucherCard(
                    discountText = "30%",
                    titleText = "Giảm 30% toàn bộ Menu Nước Size Lớn",
                    expiryDate = "Hết hạn 23/04/2024",
                    color = Color(0xFFFF9800),
                    imageRes = R.drawable.vc_1
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Voucher 2 - 40% OFF
                VoucherCard(
                    discountText = "40%",
                    titleText = "Giảm 40% + Freeship Đơn Từ 10 Ly Trở Lên",
                    expiryDate = "Hết hạn 30/04/2024",
                    color = Color(0xFFF44336),
                    freeshipTag = true,
                    imageRes = R.drawable.vc_2
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Voucher 3 - 30% OFF
                VoucherCard(
                    discountText = "30%",
                    titleText = "Giảm 30% + Freeship Đơn Từ 3 Ly",
                    expiryDate = "Hết hạn 30/04/2024",
                    color = Color(0xFFFF9800),
                    freeshipTag = true,
                    imageRes = R.drawable.vc_3
                )
            }
        }
    }
}

@Composable
fun FunctionButton(
    icon: Int,
    title: String,
    onClick: () -> Unit,
    iconColor: Color = Color(0xFFFF9800), // Default orange color like in the image
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(150.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(iconColor)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color(0xFF553311),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun VoucherCard(
    discountText: String,
    titleText: String,
    expiryDate: String,
    color: Color,
    freeshipTag: Boolean = false,
    imageRes: Int // Add parameter for custom image resource
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
            // Left side with discount tag and image
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .height(75.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Use the provided image resource
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Voucher image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Overlay discount text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = discountText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "OFF",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    if (freeshipTag) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FREESHIP",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                        }
                    }
                }
            }
            
            // Right side with details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = titleText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF553311)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = expiryDate,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
} 