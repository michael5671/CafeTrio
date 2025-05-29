package com.example.cafetrio.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cafetrio.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VoucherDetailDialog(
    isVisible: Boolean,
    isEditing: Boolean = false,
    voucherCode: String = "",
    voucherDescription: String = "",
    voucherDiscount: String = "",
    voucherMinAmount: String = "",
    voucherMaxDiscount: String = "",
    voucherStartDate: String = "",
    voucherEndDate: String = "",
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String) -> Unit
) {
    if (!isVisible) return
    
    var code by remember { mutableStateOf(voucherCode) }
    var description by remember { mutableStateOf(voucherDescription) }
    var discount by remember { mutableStateOf(voucherDiscount) }
    var minAmount by remember { mutableStateOf(voucherMinAmount) }
    var maxDiscount by remember { mutableStateOf(voucherMaxDiscount) }
    var startDate by remember { mutableStateOf(voucherStartDate) }
    var endDate by remember { mutableStateOf(voucherEndDate) }
    
    // Định dạng ngày mặc định nếu trống
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentDate = dateFormat.format(Date())
    if (startDate.isEmpty()) startDate = currentDate
    if (endDate.isEmpty()) endDate = currentDate
    
    // Kiểm tra nếu dùng phần trăm (%) hay giá trị cố định
    var isPercentage by remember { mutableStateOf(true) }
    
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
                        text = if (isEditing) "Chỉnh Sửa Voucher" else "Thêm Voucher Mới",
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
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Form nhập thông tin voucher
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    label = { Text("Mã voucher") },
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
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CafeBrown,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = CafeBrown
                    ),
                    minLines = 3,
                    maxLines = 5
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Chọn loại giảm giá
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Loại giảm giá:",
                        color = CafeBrown,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isPercentage = true }
                    ) {
                        RadioButton(
                            selected = isPercentage,
                            onClick = { isPercentage = true },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CafeBrown
                            )
                        )
                        Text(
                            text = "Phần trăm (%)",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isPercentage = false }
                    ) {
                        RadioButton(
                            selected = !isPercentage,
                            onClick = { isPercentage = false },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CafeBrown
                            )
                        )
                        Text(
                            text = "Số tiền (VNĐ)",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = discount,
                    onValueChange = { discount = it },
                    label = { Text("Giá trị giảm ${if (isPercentage) "(%)" else "(VNĐ)"}") },
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
                
                // Giá trị giảm tối đa (chỉ hiển thị khi chọn giảm theo %)
                if (isPercentage) {
                    OutlinedTextField(
                        value = maxDiscount,
                        onValueChange = { maxDiscount = it },
                        label = { Text("Giá trị giảm tối đa (VNĐ)") },
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
                }
                
                OutlinedTextField(
                    value = minAmount,
                    onValueChange = { minAmount = it },
                    label = { Text("Giá trị đơn hàng tối thiểu (VNĐ)") },
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
                
                // Thời gian hiệu lực
                Text(
                    text = "Thời gian hiệu lực:",
                    color = CafeBrown,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Chọn thời gian hiệu lực
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Ngày bắt đầu") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CafeBrown,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = CafeBrown
                        ),
                        singleLine = true,
                        placeholder = { Text("DD/MM/YYYY") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Chọn ngày",
                                tint = CafeBrown
                            )
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("Ngày kết thúc") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CafeBrown,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = CafeBrown
                        ),
                        singleLine = true,
                        placeholder = { Text("DD/MM/YYYY") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Chọn ngày",
                                tint = CafeBrown
                            )
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { 
                        if (code.isNotEmpty() && discount.isNotEmpty()) {
                            onSave(code, description, discount, minAmount, maxDiscount, startDate, endDate)
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