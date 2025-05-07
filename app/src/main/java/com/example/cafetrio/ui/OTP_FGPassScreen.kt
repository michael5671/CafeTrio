package com.example.cafetrio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.ui.components.OtpTextField
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeButtonBackground
import com.example.cafetrio.ui.theme.CafeLoginBackground
import com.example.cafetrio.ui.theme.CafeTrioTheme
import kotlinx.coroutines.delay

@Composable
fun OTP_FGPassScreen(
    emailAddress: String = "example@gmail.com",
    onBackClick: () -> Unit = {},
    onVerifyOtp: (String) -> Unit = {}
) {
    var otpValue by remember { mutableStateOf("") }
    var timeRemaining by remember { mutableStateOf(120) } // 2 phút = 120 giây
    
    // Đếm ngược thời gian
    LaunchedEffect(key1 = true) {
        while (timeRemaining > 0) {
            delay(1000) // Đợi 1 giây
            timeRemaining--
        }
    }
    
    // Format thời gian còn lại dạng mm:ss
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CafeLoginBackground),
        contentAlignment = Alignment.Center // Căn chỉnh tất cả về trung tâm
    ) {
        // Nút đóng (X) ở góc trên bên phải
        Text(
            text = "×",
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable(onClick = onBackClick)
                .padding(32.dp)
        )
        
        // Nội dung chính - căn giữa màn hình
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Tiêu đề xác nhận OTP
            Text(
                text = "Xác nhận Mã OTP",
                color = CafeBrown,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Thông báo gmail
            Text(
                text = "Mã xác thực gồm 6 số đã được gửi đến địa chỉ gmail $emailAddress",
                color = CafeBrown,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Hướng dẫn
            Text(
                text = "Nhập mã để tiếp tục",
                color = CafeBrown,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Ô nhập OTP 6 số
            OtpTextField(
                otpText = otpValue,
                onOtpTextChange = { value, isFilled ->
                    otpValue = value
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Thông báo không nhận được mã
            Text(
                text = "Bạn không nhận được mã? Gửi lại ($timeString)",
                color = CafeBrown,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Nút xác nhận
            Button(
                onClick = { onVerifyOtp(otpValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CafeButtonBackground,
                    contentColor = CafeBeige
                ),
                enabled = otpValue.length == 6
            ) {
                Text(
                    text = "XÁC NHẬN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OTP_FGPassScreenPreview() {
    CafeTrioTheme {
        OTP_FGPassScreen()
    }
} 