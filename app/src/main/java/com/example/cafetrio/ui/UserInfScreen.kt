package com.example.cafetrio.ui

import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfScreen(
    onBackClick: () -> Unit,
    onUpdateInfoClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("Tuấn Nguyễn") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Date picker
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.DatePickerTheme,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            birthDate = dateFormatter.format(calendar.time)
        },
        currentYear - 18,
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    // Gender Dropdown
    var expandedGenderDropdown by remember { mutableStateOf(false) }
    val genderOptions = listOf("Nam", "Nữ", "Khác")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Cập nhật thông tin",
                        color = CafeBrown,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CafeBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CafeBeige
                )
            )
        },
        containerColor = CafeBeige
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(CafeBeige),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.coffee_beans), // Change to an existing icon in your drawable
                    contentDescription = "Avatar Placeholder",
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên của bạn") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CafeBrown,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = CafeBrown,
                    focusedLabelColor = CafeBrown,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = CafeBrown,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email của bạn") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CafeBrown,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = CafeBrown,
                    focusedLabelColor = CafeBrown,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = CafeBrown,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Birth Date Field
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Chọn ngày sinh") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_my_calendar), // Change to an existing calendar icon
                        contentDescription = "Chọn ngày sinh",
                        tint = CafeBrown,
                        modifier = Modifier.clickable { datePickerDialog.show() }
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CafeBrown,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = CafeBrown,
                    focusedLabelColor = CafeBrown,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = CafeBrown,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Field
            ExposedDropdownMenuBox(
                expanded = expandedGenderDropdown,
                onExpandedChange = { expandedGenderDropdown = !expandedGenderDropdown },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { },
                    label = { Text("Chọn giới tính") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedGenderDropdown
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CafeBrown,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = CafeBrown,
                        focusedLabelColor = CafeBrown,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = CafeBrown,
                        unfocusedTextColor = Color.DarkGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedGenderDropdown,
                    onDismissRequest = { expandedGenderDropdown = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    genderOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, color = CafeBrown) },
                            onClick = {
                                gender = selectionOption
                                expandedGenderDropdown = false
                            },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current Password Field
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Nhập mật khẩu hiện tại") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                        Icon(
                            painter = painterResource(
                                id = if (showCurrentPassword) R.drawable.eye
                                else R.drawable.close_eye
                            ),
                            contentDescription = if (showCurrentPassword) "Hide password" else "Show password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CafeBrown,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = CafeBrown,
                    focusedLabelColor = CafeBrown,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = CafeBrown,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New Password Field
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nhập mật khẩu mới") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showNewPassword = !showNewPassword }) {
                        Icon(
                            painter = painterResource(
                                id = if (showNewPassword) R.drawable.eye
                                else R.drawable.close_eye
                            ),
                            contentDescription = if (showNewPassword) "Hide password" else "Show password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CafeBrown,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = CafeBrown,
                    focusedLabelColor = CafeBrown,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = CafeBrown,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Update Info Button
            Button(
                onClick = onUpdateInfoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CafeBrown)
            ) {
                Text("Cập nhật thông tin", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete Account Button
            TextButton(onClick = onDeleteAccountClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete), // Change to an existing delete icon
                    contentDescription = "Xóa tài khoản",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Xóa tài khoản", color = Color.Red, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
} 