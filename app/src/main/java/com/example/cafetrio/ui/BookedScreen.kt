package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookedScreen(
    onBackClick: () -> Unit = {},
    onNavigationItemClick: (String) -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F4E1)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Danh mục with dropdown icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* TODO: Handle category dropdown */ }
                    ) {
                        // Logo square dots
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Danh mục",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF553311)
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color(0xFF553311)
                        )
                    }
                },
                actions = {
                    // Search button
                    Box(
                        modifier = Modifier.padding(end = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .background(
                                    color = Color(0xFFFFFFFF), 
                                    shape = RoundedCornerShape(size = 20.dp)
                                )
                                .clickable { /* TODO: Handle search click */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    // Favorites button
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
                                    shape = RoundedCornerShape(size = 20.dp)
                                )
                                .clickable { /* TODO: Handle favorites click */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_love),
                                contentDescription = "Favorites",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
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
                    
                    // Đặt hàng (active)
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
            // Placeholder text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Màn hình đặt hàng\nĐang phát triển...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = CafeBrown,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
} 