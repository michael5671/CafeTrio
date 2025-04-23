package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.*

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit = {},
    onSubmitPhone: (String) -> Unit = {}
) {
    var phoneNumber by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CafeLoginBackground)
    ) {
        // Phần trên màn hình - khoảng trống
        Spacer(modifier = Modifier.weight(0.1f))
        
        // Phần ảnh cà phê - đặt ở giữa gần form đăng nhập
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cf_login),
                contentDescription = "Cafe Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 350.dp)
                    .offset(y = 40.dp),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.Center
            )
        }
        
        // Phần form - nối liền với ảnh cà phê
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f)
        ) {
            // Form với background màu và corner radius
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(CafeLoginBackground)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Chào mừng
                Text(
                    text = "Chào mừng bạn đến với",
                    color = CafeBrown,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                
                // Tiêu đề Café Trio
                Text(
                    text = "Café Trio",
                    color = CafeBrown,
                    fontSize = 48.sp,
                    fontFamily = FontFamily(Font(R.font.agbalumo_regular)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )
                
                // Tiêu đề Quên mật khẩu
                Text(
                    text = "Quên mật khẩu",
                    color = CafeBrown,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Hướng dẫn nhập số điện thoại với link đăng nhập
                val annotatedString = buildAnnotatedString {
                    append("Nhớ mật khẩu? ")
                    pushStringAnnotation(tag = "login", annotation = "login")
                    withStyle(
                        style = SpanStyle(
                            color = CafeBlueLink,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Đăng nhập tại đây")
                    }
                    pop()
                }
                
                Text(
                    text = annotatedString,
                    color = CafeBrown,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable(onClick = onBackToLogin)
                )
                
                // Ô nhập số điện thoại
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = { 
                        Text(
                            "Nhập số điện thoại", 
                            color = CafeGrayText,
                            fontSize = 18.sp
                        ) 
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            // Icon Vietnam 
                            Image(
                                painter = painterResource(id = R.drawable.vietnam_ic),
                                contentDescription = "Vietnam icon",
                                modifier = Modifier.size(24.dp)
                            )
                            
                            // Text +84 bên cạnh icon
                            Text(
                                text = "+84",
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            
                            HorizontalDivider(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(1.dp),
                                color = Color.LightGray
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(6.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Nút xác nhận
                Button(
                    onClick = { onSubmitPhone(phoneNumber) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CafeButtonBackground
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "XÁC NHẬN",
                        color = CafeBeige,
                        fontSize = 16.sp
                    )
                }
            }
            
            // Nút đóng (X) ở góc trên bên phải
            Text(
                text = "×",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 20.dp)
                    .clickable(onClick = onBackToLogin)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    CafeTrioTheme {
        ForgotPasswordScreen()
    }
} 