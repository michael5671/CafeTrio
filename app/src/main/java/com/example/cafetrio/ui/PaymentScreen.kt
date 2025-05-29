package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cafetrio.R
import com.example.cafetrio.data.models.Order
import com.example.cafetrio.data.models.CartItem
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeTrioTheme
import com.example.cafetrio.utils.FormatUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    order: Order,
    onBackClick: () -> Unit = {},
    onPlaceOrderClick: () -> Unit = {},
    onNavigateToMain: () -> Unit = {},
    onSelectVoucher: () -> Unit = {}
) {
    val backgroundColor = CafeBeige
    val scrollState = rememberScrollState()
    
    // State for showing success dialog
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // State for showing payment method selection
    var showPaymentMethodDialog by remember { mutableStateOf(false) }
    
    // Selected payment method
    var selectedPaymentMethod by remember { mutableStateOf("Tiền mặt") }
    
    // Bottom sheet state
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    
    // Success Dialog
    if (showSuccessDialog) {
        OrderSuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                onNavigateToMain()
            }
        )
    }
    
    // Payment Method Bottom Sheet
    if (showPaymentMethodDialog) {
        ModalBottomSheet(
            onDismissRequest = { showPaymentMethodDialog = false },
            sheetState = sheetState,
            containerColor = Color(0xFFF8F1DF),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)  // Add extra padding at bottom for better UX
            ) {
                // Header with title and close button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F1DF))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Phương thức thanh toán",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    
                    IconButton(
                        onClick = { 
                            scope.launch { 
                                sheetState.hide()
                                showPaymentMethodDialog = false 
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF553311)
                        )
                    }
                }
                
                // Instruction text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFDFD7C8))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Vui lòng chọn phương thức thanh toán phù hợp cho đơn hàng của bạn",
                        fontSize = 16.sp,
                        color = Color(0xFF553311),
                        lineHeight = 24.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Payment options section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Cách thanh toán",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Cash option
                    PaymentOptionItem(
                        name = "Tiền mặt",
                        icon = R.drawable.ic_cash,
                        isSelected = selectedPaymentMethod == "Tiền mặt",
                        onSelect = { 
                            selectedPaymentMethod = "Tiền mặt"
                            scope.launch { 
                                sheetState.hide()
                                showPaymentMethodDialog = false 
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // VNPAY option
                    PaymentOptionItem(
                        name = "VNPAY",
                        icon = R.drawable.ic_vnpay,
                        isSelected = selectedPaymentMethod == "VNPAY",
                        onSelect = { 
                            selectedPaymentMethod = "VNPAY"
                            scope.launch { 
                                sheetState.hide()
                                showPaymentMethodDialog = false 
                            }
                        }
                    )
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Xác nhận đơn hàng",
                            color = CafeBrown,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Delivery option (now includes address information)
            DeliveryOption()
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Product selected
            ProductsSection(order)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Cost summary
            CostSummarySection(order.totalAmount, onSelectVoucher)
            
            // Promotion section is now part of CostSummarySection
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Payment method
            PaymentMethodSection(
                selectedMethod = selectedPaymentMethod,
                onSelectPaymentMethod = {
                    showPaymentMethodDialog = true
                }
            )
            
            // Place order button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { 
                        // Show success dialog instead of navigating immediately
                        showSuccessDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7D5A43)
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Đặt hàng",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DeliveryOption() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F1DF))
            .padding(vertical = 8.dp)
    ) {
        // Tiêu đề và nút "Thay đổi"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Giao hàng tận nơi",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF553311)
            )
            
            Button(
                onClick = { /* Xử lý khi nhấn Thay đổi */ },
                modifier = Modifier
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7D5A43)
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Thay đổi",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // Thông tin địa chỉ giao hàng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Nguyễn Đình Tuấn",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF553311)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Lô T2-1,2, Đường D1, Quận 9, Hồ Chí Minh, Việt Nam",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Chi tiết",
                tint = Color.Gray
            )
        }
        
        // Phần thời gian giao hàng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "15 - 30 phút",
                    fontSize = 15.sp,
                    color = Color(0xFF553311)
                )
                
                Text(
                    text = "Càng sớm càng tốt",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF553311)
                )
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Chi tiết",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun ProductsSection(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sản phẩm đã chọn",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF553311)
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.items.size} món",
                    fontSize = 14.sp,
                    color = Color(0xFF7D5A43),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 8.dp)
                )
                
                Button(
                    onClick = { /* Xử lý khi nhấn Thêm */ },
                    modifier = Modifier
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7D5A43)
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "+ Thêm",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // List of products
        order.items.forEach { item ->
            // Hiển thị sản phẩm với icon cafe
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon cafe
                Image(
                    painter = painterResource(id = R.drawable.cup_of_cf),
                    contentDescription = "Coffee icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                
                // Product info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "x${item.quantity} ${item.productName}",
                            fontSize = 14.sp,
                            color = Color(0xFF553311),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Text(
                            text = FormatUtils.formatPrice(item.price * item.quantity),
                            fontSize = 14.sp,
                            color = Color(0xFF553311),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Size
                    Text(
                        text = "${item.size}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            
            // Show toppings if any
            if (item.toppings.isNotEmpty() || item.note.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 4.dp, bottom = 8.dp)
                ) {
                    if (item.toppings.isNotEmpty()) {
                        Text(
                            text = "Topping: ${item.toppings.joinToString(", ")}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    
                    if (item.note.isNotEmpty()) {
                        Text(
                            text = "Ghi chú: ${item.note}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CostSummarySection(total: Int, onSelectVoucher: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F1DF))
    ) {
        // Header
        Text(
            text = "Tổng cộng",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF553311),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        
        // Thành tiền
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Thành tiền",
                fontSize = 16.sp,
                color = Color(0xFF553311)
            )
            
            Text(
                text = FormatUtils.formatPrice(total),
                fontSize = 16.sp,
                color = Color(0xFF553311),
                fontWeight = FontWeight.Medium
            )
        }
        
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        
        // Phí giao hàng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Phí giao hàng",
                fontSize = 16.sp,
                color = Color(0xFF553311)
            )
            
            Text(
                text = FormatUtils.formatPrice(0),
                fontSize = 16.sp,
                color = Color(0xFF553311),
                fontWeight = FontWeight.Medium
            )
        }
        
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        
        // Chọn khuyến mãi/Đổi bean
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSelectVoucher)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chọn khuyến mãi/đổi bean",
                fontSize = 16.sp,
                color = Color(0xFF3DA9FC)
            )
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Chi tiết",
                tint = Color.Gray
            )
        }
        
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        
        // Số tiền thanh toán
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Số tiền thanh toán",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF553311)
            )
            
            Text(
                text = FormatUtils.formatPrice(total),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF553311)
            )
        }
    }
}

@Composable
fun PaymentMethodSection(
    selectedMethod: String,
    onSelectPaymentMethod: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F1DF))
            .padding(vertical = 8.dp)
    ) {
        // Section header
        Text(
            text = "Thanh toán",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF553311),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Payment method selection row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectPaymentMethod() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Payment icon
                val icon = if (selectedMethod == "Tiền mặt") {
                    R.drawable.ic_cash
                } else {
                    R.drawable.ic_vnpay
                }
                
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Payment Method Icon",
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                )
                
                Text(
                    text = selectedMethod,
                    fontSize = 16.sp,
                    color = Color(0xFF553311),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Chi tiết",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun PaymentOptionItem(
    name: String,
    icon: Int,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Radio button
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF553311),
                unselectedColor = Color.Gray
            )
        )
        
        // Icon
        Image(
            painter = painterResource(id = icon),
            contentDescription = name,
            modifier = Modifier
                .size(36.dp)
                .padding(horizontal = 8.dp)
        )
        
        // Method name
        Text(
            text = name,
            fontSize = 16.sp,
            color = Color(0xFF553311),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun OrderSuccessDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF8F1DF))
                .padding(24.dp)
        ) {
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Đóng",
                    tint = Color(0xFF553311)
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Leave space for the close button
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Thông báo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF553311),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Đặt hàng thành công!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF553311),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Đơn hàng của bạn sẽ sớm được giao đến!",
                    fontSize = 16.sp,
                    color = Color(0xFF553311),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    CafeTrioTheme {
        val previewOrder = Order(
            id = 1,
            customerName = "Nguyễn Đình Tuấn",
            phoneNumber = "0981234567",
            items = listOf(
                com.example.cafetrio.data.models.CartItem(
                    id = "1",
                    productName = "Smoothie Xoài Nhiệt Đới Granola",
                    size = "Vừa",
                    quantity = 1,
                    price = 65000,
                    toppings = listOf("Trân châu trắng")
                )
            ),
            totalAmount = 65000
        )
        PaymentScreen(order = previewOrder)
    }
}

@Preview(showBackground = true)
@Composable
fun OrderSuccessDialogPreview() {
    CafeTrioTheme {
        OrderSuccessDialog(onDismiss = {})
    }
} 