package com.example.cafetrio.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cafetrio.ui.theme.*

@Composable
fun ProductDetailDialog(
    isVisible: Boolean,
    isEditing: Boolean = false,
    productName: String = "",
    productPrice: String = "",
    productDescription: String = "",
    productCategory: String = "",
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    if (!isVisible) return
    
    var name by remember { mutableStateOf(productName) }
    var price by remember { mutableStateOf(productPrice) }
    var description by remember { mutableStateOf(productDescription) }
    var selectedCategory by remember { mutableStateOf(productCategory) }
    var hasImage by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    
    // Danh sách danh mục mẫu
    val categories = listOf("Cà phê", "Trà sữa", "Nước ép", "Sinh tố", "Đồ ăn nhẹ")
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEditing) "Chỉnh Sửa Sản Phẩm" else "Thêm Sản Phẩm Mới",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CafeBrown
                    )
                    
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = CafeBrown
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Phần tải lên hình ảnh
                ImageUploadSection(hasImage = hasImage, onImageSelected = { hasImage = true })
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Form nhập thông tin
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên sản phẩm") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CafeBrown,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = CafeBrown
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Giá bán (VNĐ)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CafeBrown,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = CafeBrown
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Dropdown chọn danh mục
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = { },
                        label = { Text("Danh mục") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CafeBrown,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = CafeBrown
                        ),
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = CafeBrown,
                                modifier = Modifier.clickable { isDropdownExpanded = true }
                            )
                        }
                    )
                    
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color.White)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(text = category) },
                                onClick = {
                                    selectedCategory = category
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CafeBrown,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = CafeBrown
                    ),
                    minLines = 4,
                    maxLines = 6
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { 
                        if (name.isNotEmpty() && price.isNotEmpty() && selectedCategory.isNotEmpty()) {
                            onSave(name, price, description, selectedCategory)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CafeButtonBackground
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isEditing) "Cập Nhật" else "Thêm Mới",
                        fontSize = 16.sp,
                        color = CafeBeige
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    isVisible: Boolean,
    itemName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!isVisible) return
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Xác nhận xóa",
                color = CafeBrown,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Bạn có chắc chắn muốn xóa \"$itemName\"?",
                color = Color.DarkGray
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373)
                )
            ) {
                Text("Xóa")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CafeBrown
                )
            ) {
                Text("Hủy")
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun ImageUploadSection(
    hasImage: Boolean = false,
    onImageSelected: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CafeBeige.copy(alpha = 0.3f))
                .clickable { onImageSelected() },
            contentAlignment = Alignment.Center
        ) {
            if (hasImage) {
                // Hiển thị hình ảnh đã tải lên
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CafeBeige)
                )
            } else {
                // Hiển thị dấu cộng hoặc biểu tượng tải lên
                Text(
                    text = "+",
                    color = CafeBrown,
                    fontSize = 40.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Tải ảnh sản phẩm",
            color = CafeBrown,
            fontSize = 14.sp
        )
    }
} 