package com.example.cafetrio.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeButtonBackground
import com.example.cafetrio.ui.theme.CafeGrayText
import com.example.cafetrio.ui.theme.CafeLoginBackground
import com.example.cafetrio.ui.theme.CafeTrioTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBackClick: () -> Unit = {},
    onSignUpSubmit: (String) -> Unit = {}
) {
    var phoneNumber by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // Dropdown menu state
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Nam", "Nữ", "Khác")
    
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    
    // Date picker
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Validate date format when manually entered
    val validateAndUpdateDate = { input: String ->
        try {
            if (input.isEmpty()) {
                birthday = input
            } else {
                // Attempt to parse the date to validate it
                val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                df.isLenient = false
                df.parse(input)
                birthday = input
            }
        } catch (e: Exception) {
            // If parsing fails, leave the field as is - could also show error
        }
    }
    
    // Year first date picker dialog
    val datePickerDialog = DatePickerDialog(
        context,
        R.style.DatePickerTheme,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            birthday = dateFormatter.format(calendar.time)
        },
        currentYear - 18, // Default to 18 years ago
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    
    // Setup date picker to show year selection first
    datePickerDialog.datePicker.apply {
        // First set the max date to current date (can't choose future dates)
        maxDate = System.currentTimeMillis()
        
        // Default year range: current year - 80 years to current year
        // This ensures easier selection for older people
        updateDate(currentYear - 18, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }
    
    // Form validation
    val isFormValid = phoneNumber.isNotEmpty() && 
                     fullName.isNotEmpty() && 
                     birthday.isNotEmpty() && 
                     gender.isNotEmpty() && 
                     password.isNotEmpty() && 
                     password == confirmPassword
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CafeLoginBackground),
        contentAlignment = Alignment.Center
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
                .padding(16.dp)
        )
        
        // Nội dung chính - căn giữa màn hình
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Tiêu đề chào mừng
            Text(
                text = "Chào mừng bạn đến với",
                color = CafeBrown,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Logo Café Trio
            Text(
                text = "Café Trio",
                color = CafeBrown,
                fontSize = 48.sp,
                fontFamily = FontFamily(Font(R.font.agbalumo_regular)),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // Tiêu đề đăng ký
            Text(
                text = "Đăng ký",
                color = CafeBrown,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            
            // Subtitle
            Text(
                text = buildAnnotatedString {
                    append("Điền các thông tin sau để trở thành\nthành viên của ")
                    withStyle(SpanStyle(fontFamily = FontFamily(Font(R.font.agbalumo_regular)))) {
                        append("Café Trio")
                    }
                    append(" bạn nhé!")
                },
                color = CafeBrown,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            
            // Ô nhập số điện thoại (giống LoginScreen)
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
                        fontSize = 16.sp
                    ) 
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
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
                        
                        // Divider
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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Ô nhập họ tên
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        "Nhập họ và tên",
                        color = CafeGrayText,
                        fontSize = 16.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                shape = RoundedCornerShape(6.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Ô chọn ngày sinh - cho phép nhập thủ công hoặc chọn từ lịch
            OutlinedTextField(
                value = birthday,
                onValueChange = { input -> validateAndUpdateDate(input) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        "Chọn ngày sinh (DD/MM/YYYY)",
                        color = CafeGrayText,
                        fontSize = 16.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_my_calendar), 
                        contentDescription = "Select date",
                        tint = CafeGrayText,
                        modifier = Modifier.clickable {
                            focusManager.clearFocus()
                            datePickerDialog.show()
                        }
                    )
                }
            )
            
            // Hint về định dạng ngày
            Text(
                text = "Định dạng: Ngày/Tháng/Năm (VD: 15/05/2004)",
                color = CafeGrayText,
                fontSize = 11.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 4.dp, top = 2.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Dropdown menu for gender
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .menuAnchor(),
                    placeholder = {
                        Text(
                            "Chọn giới tính",
                            color = CafeGrayText,
                            fontSize = 16.sp
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Ô nhập mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        "Nhập mật khẩu",
                        color = CafeGrayText,
                        fontSize = 16.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(6.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Ô xác nhận mật khẩu
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        "Xác nhận mật khẩu",
                        color = CafeGrayText,
                        fontSize = 16.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (isFormValid) {
                            onSignUpSubmit(phoneNumber)
                        }
                    }
                ),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(6.dp)
            )
            
            // Hiển thị thông báo nếu mật khẩu không khớp
            if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                Text(
                    text = "Mật khẩu không khớp",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Nút xác nhận
            Button(
                onClick = { 
                    if (isFormValid) {
                        onSignUpSubmit(phoneNumber) 
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CafeButtonBackground,
                    contentColor = CafeBeige
                ),
                enabled = isFormValid
            ) {
                Text(
                    text = "XÁC NHẬN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    CafeTrioTheme {
        SignUpScreen()
    }
} 