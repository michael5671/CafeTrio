package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.animation.core.tween

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onNavigateToNoti: () -> Unit = {}
) {
    val userName = "NGUYEN DINH TUAN"
    val userCode = "CFT02809"
    val beanCount = 88
    
    val adImages = listOf(
        R.drawable.ad_1,
        R.drawable.ad_2,
        R.drawable.ad_3,
        R.drawable.ad_4,
        R.drawable.ad_5,
        R.drawable.ad_6,
        R.drawable.ad_7
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cup_of_cf),
                            contentDescription = "Café Icon",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Café nhé, bạn ơi!",
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
                                .clickable { onNavigateToNoti() },
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
                containerColor = Color(0xFFF8F4E1),
                contentColor = CafeBrown
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* TODO: Navigate to Home */ }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Trang chủ",
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
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate("order") }
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
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate("rewards") }
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
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate("differ") }
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
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F4E1))
                .verticalScroll(scrollState)
        ) {
            // User Profile Card - Redesigned with gradients
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFAF8F6F), // Lighter brown
                                    Color(0xFF74512D)  // Darker brown
                                )
                            )
                        )
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                ) {
                    Column {
                        // Top row with Café Trio, username and membership level badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Left side - Café Trio
                            Text(
                                text = "Café Trio",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(R.font.agbalumo_regular)),
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            
                            // Middle - Username
                            Text(
                                text = userName,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )
                            
                            // Right side - Orange membership badge with custom shape
                            Box(
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(30.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFFBB063), // Light orange
                                                Color(0xFFC05D0D)  // Dark orange
                                            )
                                        ),
                                        shape = RoundedCornerShape(
                                            topStart = 15.dp, 
                                            topEnd = 0.dp, 
                                            bottomStart = 15.dp, 
                                            bottomEnd = 0.dp
                                        )
                                    )
                                    .padding(start = 12.dp)
                                    .align(Alignment.CenterVertically),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Hạng ĐỒNG",
                                    color = Color(0xFF74512D),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Membership ID
                        Text(
                            text = "Mã thành viên: $userCode",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Bean balance row with "Đổi BEAN" button next to it
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Bean balance
                            Text(
                                text = "Số dư BEAN: 0",
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            
                            // Bean exchange button
                            Box(
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(30.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFFBB063), // Light orange
                                                Color(0xFFC05D0D)  // Dark orange
                                            )
                                        ),
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(horizontal = 8.dp)
                                    .clickable { onNavigate("rewards") }
                                    .align(Alignment.CenterVertically),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Đổi BEAN",
                                    color = Color(0xFF74512D),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            // Empty weight to push content to the left
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            
            // Function buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp, 
                            color = Color(0xFF000000), 
                            shape = RoundedCornerShape(size = 15.dp)
                        )
                        .width(381.dp)
                        .height(100.dp)
                        .background(
                            color = Color(0xFFF8F4E1), 
                            shape = RoundedCornerShape(size = 15.dp)
                        )
                        .padding(start = 28.dp, top = 10.dp, end = 28.dp, bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Giao hàng
                        FunctionButton(
                            icon = R.drawable.shipping,
                            text = "Giao hàng",
                            onClick = { onNavigate("order") },
                            modifier = Modifier
                                .width(58.dp)
                                .height(71.dp)
                        )
                        
                        // Mang đi
                        FunctionButton(
                            icon = R.drawable.take_away,
                            text = "Mang đi",
                            onClick = { onNavigate("order") },
                            modifier = Modifier
                                .width(58.dp)
                                .height(71.dp)
                                .padding(start = 4.dp, end = 4.dp)
                        )
                        
                        // Đơn hàng
                        FunctionButton(
                            icon = R.drawable.invoice,
                            text = "Đơn hàng",
                            onClick = { onNavigate("orders") },
                            modifier = Modifier
                                .width(58.dp)
                                .height(71.dp)
                                .padding(start = 2.dp, end = 1.dp)
                        )
                        
                        // Đổi Bean
                        FunctionButton(
                            icon = R.drawable.coffee_beans,
                            text = "Đổi Bean",
                            onClick = { onNavigate("rewards") },
                            modifier = Modifier
                                .width(58.dp)
                                .height(71.dp)
                                .padding(start = 4.dp, end = 3.dp)
                        )
                    }
                }
            }
            
            // Advertisement Carousel with Pager
            val pagerState = rememberPagerState(pageCount = { adImages.size })
            val coroutineScope = rememberCoroutineScope()
            
            // Track whether we're in transition between ad_7 and ad_1
            var isResettingCarousel by remember { mutableStateOf(false) }
            
            // Auto slide effect for ads
            LaunchedEffect(Unit) {
                while(true) {
                    delay(3000)  
                    if (pagerState.currentPage < adImages.size - 1) {
                        // Normal transition for ads 1-6
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1,
                                animationSpec = tween(durationMillis = 2000)
                            )
                        }
                    } else if (!isResettingCarousel) {
                        // Special transition for ad_7 to ad_1: 
                        // First mark that we're in transition
                        isResettingCarousel = true
                        
                        // Allow the last ad to be viewed for the normal duration
                        delay(3000)
                        
                        // Then instantly jump to the first page without animation
                        coroutineScope.launch {
                            pagerState.scrollToPage(0)
                            // Reset the transition flag after a small delay
                            delay(100)
                            isResettingCarousel = false
                        }
                    }
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(vertical = 8.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) { page ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(horizontal = 4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = adImages[page]),
                            contentDescription = "Advertisement",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                
                // Indicators for the current ad
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(adImages.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index) CafeBrown 
                                    else CafeBrown.copy(alpha = 0.5f)
                                )
                        )
                    }
                }
            }
            
            // Separator
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFFD9D9D9),
                thickness = 1.dp
            )
            
            // Phần "Món Mới Phải Thử"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Tiêu đề "Món Mới Phải Thử"
                Text(
                    text = "Món Mới Phải Thử",
                    color = Color(0xFF543310),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Grid layout cho các sản phẩm (2 sản phẩm mỗi hàng)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Smoothie Xoài Nhiệt Đới Granola
                    ProductItem(
                        image = R.drawable.xoai_granola,
                        name = "Smoothie Xoài Nhiệt Đới Granola",
                        price = "65.000đ",
                        isNew = true,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("xoai_granola") }
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Smoothie Phúc Bồn Tử Granola
                    ProductItem(
                        image = R.drawable.phuc_bon_tu_granola,
                        name = "Smoothie Phúc Bồn Tử Granola",
                        price = "65.000đ",
                        isNew = true,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("phuc_bon_tu_granola") }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Hàng thứ 2 với 2 sản phẩm
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Oolong Tứ Quý Vải
                    ProductItem(
                        image = R.drawable.oolong_tu_quy_vai,
                        name = "Oolong Tứ Quý Vải",
                        price = "59.000đ",
                        isNew = true,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("oolong_tu_quy_vai") }
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Oolong Tứ Quý Kim Quất Trân Châu
                    ProductItem(
                        image = R.drawable.oolong_kim_quat_tran_chau,
                        name = "Oolong Tứ Quý Kim Quất Trân Châu",
                        price = "59.000đ",
                        isNew = true,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("oolong_kim_quat_tran_chau") }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Hàng thứ 3 với 1 sản phẩm và 1 chỗ trống
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Trà Sữa Oolong Tứ Quý Sương Sáo
                    ProductItem(
                        image = R.drawable.tra_sua_oolong_tu_quy_suong_sao,
                        name = "Trà Sữa Oolong Tứ Quý Sương Sáo",
                        price = "55.000đ",
                        isNew = true,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("tra_sua_oolong_tu_quy_suong_sao") }
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Placeholder để giữ layout cân đối
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    image: Int,
    name: String,
    price: String,
    isNew: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
    ) {
        // Hình ảnh sản phẩm với badge "NEW" nếu cần
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
            
            // Badge "NEW" ở góc trên bên trái
            if (isNew) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                        .background(Color(0xFFFF3333), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "NEW",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Tên sản phẩm
        Text(
            text = name,
            color = Color(0xFF543310),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Giá và nút thêm
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = price,
                color = Color(0xFF543310),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            // Nút thêm sản phẩm
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF74512D), CircleShape)
                    .clickable { /* TODO: Handle add product */ },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button_plus),
                    contentDescription = "Add to cart",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun FunctionButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF8F4E1))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier.size(36.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = text,
            color = Color(0xFF543310),
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CafeTrioTheme {
        MainScreen()
    }
} 