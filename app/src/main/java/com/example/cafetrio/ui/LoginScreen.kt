package com.example.cafetrio.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.data.api.ApiClient
import com.example.cafetrio.data.dto.LoginRequest
import com.example.cafetrio.data.dto.LoginResponse
import android.content.Context
import com.example.cafetrio.ui.theme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun LoginScreen(
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    
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
                .weight(0.6f),
            contentAlignment = Alignment.Center
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
        
        // Phần form đăng nhập - nối liền với ảnh cà phê
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f)
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
            
            // Ô nhập email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { 
                    Text(
                        "Nhập địa chỉ gmail", 
                        color = CafeGrayText,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 0.dp)
                    ) 
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(6.dp)
            )
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // Ô nhập mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Nhập mật khẩu", color = CafeGrayText, fontSize = 18.sp) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true,
                shape = RoundedCornerShape(6.dp)
            )
            
            // Dòng Ghi nhớ tôi và Quên mật khẩu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { rememberMe = !rememberMe }
                ) {
                    // Checkbox tùy chỉnh
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .border(1.dp, CafeBrown, RoundedCornerShape(6.dp))
                            .background(
                                if (rememberMe) CafeBrown else Color.Transparent,
                                RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (rememberMe) {
                            Spacer(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(
                                        CafeLoginBackground,
                                        RoundedCornerShape(2.5.dp)
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ghi nhớ tôi",
                        color = CafeBrown,
                        fontSize = 12.sp
                    )
                }
                
                Text(
                    text = "Quên mật khẩu?",
                    color = CafeBrown,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onForgotPasswordClick() }
                )
            }
            
            // Nút đăng nhập
            Button(
                onClick = { 
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        val loginRequest = LoginRequest(
                            email = email,
                            password = password,
                            rememberMe = rememberMe
                        )
                        
                        ApiClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    val loginResponse = response.body()
                                    if (loginResponse != null) {
                                        // Lưu token và thông tin người dùng vào SharedPreferences
                                        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        
                                        // Lưu thông tin đăng nhập
                                        editor.putString("auth_token", loginResponse.token)
                                        editor.putString("user_id", loginResponse.userId)
                                        editor.putString("full_name", loginResponse.fullName)
                                        editor.putString("email", loginResponse.email)
                                        editor.apply()
                                        
                                        Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                                        onLoginClick()
                                    }
                                } else {
                                    val errorMsg = when(response.code()) {
                                        401 -> "Email hoặc mật khẩu không đúng"
                                        404 -> "Tài khoản không tồn tại"
                                        else -> "Đăng nhập thất bại: ${response.code()}"
                                    }
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                }
                            }
                            
                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CafeButtonBackground
                ),
                shape = RoundedCornerShape(6.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = CafeBeige,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Đăng Nhập",
                        color = CafeBeige,
                        fontSize = 16.sp
                    )
                }
            }
            
            // Phần đăng ký tài khoản mới
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bạn chưa có tài khoản? ",
                    color = CafeBrown,
                    fontSize = 12.sp
                )
                Text(
                    text = "Đăng ký ngay",
                    color = CafeLightBrown,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    CafeTrioTheme {
        LoginScreen()
    }
} 