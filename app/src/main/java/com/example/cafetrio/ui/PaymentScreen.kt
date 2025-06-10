package com.example.cafetrio.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.cafetrio.data.api.ApiClient
import com.example.cafetrio.data.dto.OrderDetail
import com.example.cafetrio.data.dto.PaymentRequest
import com.example.cafetrio.data.dto.PaymentResponse
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeTrioTheme
import com.example.cafetrio.utils.FormatUtils
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    order: OrderDetail,
    onBackClick: () -> Unit = {},
    onNavigateToMain: () -> Unit = {},
    onSelectVoucher: () -> Unit = {}
) {
    val backgroundColor = CafeBeige
    val scrollState = rememberScrollState()
    val context = LocalContext.current // Lấy context để mở URL

    // State for showing success dialog
    var showSuccessDialog by remember { mutableStateOf(false) }

    // State for showing payment method selection
    var showPaymentMethodDialog by remember { mutableStateOf(false) }

    // Selected payment method
    var selectedPaymentMethod by remember { mutableStateOf("Tiền mặt") }

    // Bottom sheet state
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // State for payment error or URL
    var paymentError by remember { mutableStateOf<String?>(null) }
    var paymentUrl by remember { mutableStateOf<String?>(null) }

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
                    .padding(bottom = 32.dp)
            ) {
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
            DeliveryOption()
            Spacer(modifier = Modifier.height(8.dp))
            ProductsSection(order)
            Spacer(modifier = Modifier.height(8.dp))
            CostSummarySection(order.totalPrice, onSelectVoucher)
            Spacer(modifier = Modifier.height(8.dp))
            PaymentMethodSection(
                selectedMethod = selectedPaymentMethod,
                onSelectPaymentMethod = {
                    showPaymentMethodDialog = true
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            paymentError = null
                            paymentUrl = null
                            try {
                                val paymentRequest = PaymentRequest(
                                    amount = order.totalPrice,
                                    orderId = order.id,
                                    language = "vn"
                                )
                                val call = ApiClient.apiService.payWithVnpay(paymentRequest)
                                call.enqueue(object : Callback<PaymentResponse> {
                                    override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                                        if (response.isSuccessful) {
                                            val paymentResponse = response.body()
                                            if (paymentResponse?.code == "ok") {
                                                paymentUrl = paymentResponse.paymentUrl
                                                paymentUrl?.let { url ->
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                    context.startActivity(intent)
                                                }
                                            } else {
                                                paymentError = "Thanh toán thất bại: ${paymentResponse?.message}"
                                            }
                                        } else {
                                            paymentError = "Thanh toán thất bại: ${response.message()}"
                                        }
                                    }

                                    override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                                        paymentError = "Lỗi thanh toán: ${t.message}"
                                        Log.e("PaymentScreen", "Payment Failure: ${t.message}", t)
                                    }
                                })
                            } catch (e: Exception) {
                                paymentError = "Lỗi khi gọi API: ${e.message}"
                                Log.e("PaymentScreen", "Payment Error: ${e.message}", e)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7D5A43)
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Đặt hàng", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            paymentError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Giao hàng tận nơi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF553311))
            Button(
                onClick = { /* Xử lý khi nhấn Thay đổi */ },
                modifier = Modifier.height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D5A43)),
                shape = RoundedCornerShape(50)
            ) {
                Text("Thay đổi", color = Color.White, fontSize = 14.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Nguyễn Đình Tuấn", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF553311))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lô T2-1,2, Đường D1, Quận 9, Hồ Chí Minh, Việt Nam",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Chi tiết", tint = Color.Gray)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("15 - 30 phút", fontSize = 15.sp, color = Color(0xFF553311))
                Text("Càng sớm càng tốt", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF553311))
            }
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Chi tiết", tint = Color.Gray)
        }
    }
}

@Composable
fun ProductsSection(order: OrderDetail) {
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
            Text("Sản phẩm đã chọn", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF553311))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${order.orderItemList.size} món",
                    fontSize = 14.sp,
                    color = Color(0xFF7D5A43),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Button(
                    onClick = { /* Xử lý khi nhấn Thêm */ },
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D5A43)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("+ Thêm", color = Color.White, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        order.orderItemList.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cup_of_cf),
                    contentDescription = "Coffee icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "x${item.amount} ${item.name}",
                            fontSize = 14.sp,
                            color = Color(0xFF553311),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = FormatUtils.formatPrice(item.price?.times(item.amount) ?: 0),
                            fontSize = 14.sp,
                            color = Color(0xFF553311),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text("Vừa", fontSize = 14.sp, color = Color.Gray)
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
        Text("Tổng cộng", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF553311), modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Thành tiền", fontSize = 16.sp, color = Color(0xFF553311))
            Text(FormatUtils.formatPrice(total), fontSize = 16.sp, color = Color(0xFF553311), fontWeight = FontWeight.Medium)
        }
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Phí giao hàng", fontSize = 16.sp, color = Color(0xFF553311))
            Text(FormatUtils.formatPrice(0), fontSize = 16.sp, color = Color(0xFF553311), fontWeight = FontWeight.Medium)
        }
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSelectVoucher)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Chọn khuyến mãi/đổi bean", fontSize = 16.sp, color = Color(0xFF3DA9FC))
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Chi tiết", tint = Color.Gray)
        }
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Số tiền thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF553311))
            Text(FormatUtils.formatPrice(total), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF553311))
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
        Text(
            text = "Thanh toán",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF553311),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectPaymentMethod() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = if (selectedMethod == "Tiền mặt") R.drawable.ic_cash else R.drawable.ic_vnpay
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Payment Method Icon",
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                )
                Text(selectedMethod, fontSize = 16.sp, color = Color(0xFF553311), fontWeight = FontWeight.Medium)
            }
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Chi tiết", tint = Color.Gray)
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
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF553311),
                unselectedColor = Color.Gray
            )
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = name,
            modifier = Modifier
                .size(36.dp)
                .padding(horizontal = 8.dp)
        )
        Text(name, fontSize = 16.sp, color = Color(0xFF553311), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
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
                    .padding(top = 8.dp),
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
