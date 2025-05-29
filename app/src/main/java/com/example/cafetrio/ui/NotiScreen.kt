package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.CafeBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotiScreen(
    onBackClick: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F1DF)
    
    // Sample notifications data
    val notifications = remember {
        listOf(
            NotificationItem(
                title = "Chào bạn mới",
                message = "Chào mừng bạn đã trở thành viên của Café Trio, chúng tôi luôn mong muốn mang đến cho bạn những trải nghiệm tốt nhất!",
                timestamp = "10/04",
                isRead = false
            ),
            NotificationItem(
                title = "Chào bạn mới",
                message = "Chào mừng bạn đã trở thành viên của Café Trio, chúng tôi luôn mong muốn mang đến cho bạn những trải nghiệm tốt nhất!",
                timestamp = "10/04",
                isRead = false
            ),
            NotificationItem(
                title = "Chào bạn mới",
                message = "Chào mừng bạn đã trở thành viên của Café Trio, chúng tôi luôn mong muốn mang đến cho bạn những trải nghiệm tốt nhất!",
                timestamp = "10/04",
                isRead = false
            )
        )
    }

    var notificationsList by remember { mutableStateOf(notifications) }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thông báo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CafeBrown
                    )
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
                actions = {
                    // Mark all as read button
                    IconButton(
                        onClick = {
                            notificationsList = notificationsList.map { it.copy(isRead = true) }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_checked),
                            contentDescription = "Đánh dấu tất cả là đã đọc",
                            modifier = Modifier.size(24.dp)
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notificationsList) { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle notification click */ },
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color(0xFFF8F1DF) else Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Notification icon
            Image(
                painter = painterResource(id = R.drawable.noti_welcome),
                contentDescription = "Notification icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // Notification content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CafeBrown
                )
                
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Timestamp
            Text(
                text = notification.timestamp,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

data class NotificationItem(
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean
) 