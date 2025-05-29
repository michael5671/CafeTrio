package com.example.cafetrio.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeTrioTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.buildAnnotatedString
import com.example.cafetrio.data.CartManager
import com.example.cafetrio.data.models.CartItem
import com.example.cafetrio.utils.FormatUtils
import java.util.UUID
import com.example.cafetrio.data.WishlistManager
import com.example.cafetrio.data.models.WishlistItem
import com.example.cafetrio.data.api.ApiClient
import com.example.cafetrio.data.dto.ProductDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrdScreen(
    productId: String,
    onBackClick: () -> Unit = {},
    onViewCart: () -> Unit = {},
    onNavigateToMain: () -> Unit = {}
) {
    var showSuccessDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var product by remember { mutableStateOf<com.example.cafetrio.data.dto.ProductDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch product detail
    LaunchedEffect(productId) {
        isLoading = true
        error = null
        ApiClient.apiService.getProductDetail(productId).enqueue(object : Callback<ProductDetailResponse> {
            override fun onResponse(
                call: Call<ProductDetailResponse>,
                response: Response<ProductDetailResponse>
            ) {
                if (response.isSuccessful && response.body()?.data != null) {
                    product = response.body()!!.data
                    isLoading = false
                } else {
                    error = "Không tìm thấy sản phẩm"
                    isLoading = false
                }
            }
            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                error = "Lỗi kết nối"
                isLoading = false
            }
        })
    }

    // State for wishlist button
    val wishlistManager = remember { com.example.cafetrio.data.WishlistManager.getInstance() }
    var isInWishlist by remember { mutableStateOf(wishlistManager.isItemInWishlist(productId)) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    val backgroundColor = Color(0xFFF8F1DF)
    val cartManager = remember { com.example.cafetrio.data.CartManager.getInstance() }

    if (showSuccessDialog) {
        Dialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateToMain()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFFFDF6E3))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF5D4037),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                                .clickable {
                                    showSuccessDialog = false
                                    onNavigateToMain()
                                }
                        )
                    }
                    Text(
                        text = "Thông báo",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Chọn sản phẩm thành công!",
                        fontSize = 18.sp,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Kiểm tra đơn hàng của bạn tại ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D4037)
                                )
                            ) {
                                append("Đơn hàng")
                            }
                        },
                        fontSize = 16.sp,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                showSuccessDialog = false
                                onViewCart()
                            }
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = CafeBrown
                        )
                    }
                },
                actions = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error ?: "Lỗi")
            }
        } else if (product != null) {
            val priceString = "${product!!.price}đ"
            val description = product!!.description
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(backgroundColor)
                    .verticalScroll(rememberScrollState())
            ) {
                // Product image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    AsyncImage(
                        model = product!!.imageUrl,
                        contentDescription = product!!.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopStart)
                            .background(Color(0xFFFF3333), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "NEW",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // Product info
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
                            text = product!!.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (isInWishlist) {
                                    wishlistManager.removeItem(productId)
                                } else {
                                    wishlistManager.addItem(
                                        com.example.cafetrio.data.models.WishlistItem(
                                            id = productId,
                                            name = product!!.name,
                                            price = priceString,
                                            imageRes = 0 // Không có resource nội bộ, chỉ có url
                                        )
                                    )
                                }
                                isInWishlist = !isInWishlist
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = com.example.cafetrio.R.drawable.ic_love),
                                contentDescription = "Yêu thích",
                                tint = if (isInWishlist) Color.Red else Color.Gray,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Text(
                        text = priceString,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5D4037)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF5D4037)
                    )
                    if (isDescriptionExpanded) {
                        Text(
                            text = "Rút gọn",
                            fontSize = 14.sp,
                            color = Color(0xFF8D6E63),
                            modifier = Modifier.clickable { isDescriptionExpanded = false }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    var quantity by remember { mutableStateOf(1) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF9E8D9))
                                .clickable {
                                    if (quantity > 1) quantity--
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "−",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D4037)
                            )
                        }
                        Text(
                            text = quantity.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .width(30.dp),
                            textAlign = TextAlign.Center
                        )
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF9E8D9))
                                .clickable { quantity++ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D4037)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                val cartItem = com.example.cafetrio.data.models.CartItem(
                                    id = java.util.UUID.randomUUID().toString(),
                                    productName = product!!.name,
                                    size = "",
                                    quantity = quantity,
                                    price = product!!.price,
                                    toppings = emptyList(),
                                    note = ""
                                )
                                cartManager.addToCart(cartItem)
                                showSuccessDialog = true
                            },
                            modifier = Modifier
                                .height(56.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5D4037)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Thêm vào giỏ hàng",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SizeOption(
    size: String,
    price: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio button for size
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color(0xFF5D4037) else Color.Gray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5D4037))
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = size,
                fontSize = 16.sp,
                color = Color(0xFF5D4037)
            )
        }
        
        Text(
            text = price,
            fontSize = 16.sp,
            color = Color(0xFF5D4037),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun ToppingOption(
    name: String,
    price: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox for topping
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color(0xFF5D4037) else Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(if (isSelected) Color(0xFF5D4037) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = name,
                fontSize = 16.sp,
                color = Color(0xFF5D4037)
            )
        }
        
        Text(
            text = price,
            fontSize = 16.sp,
            color = Color(0xFF5D4037),
            textAlign = TextAlign.End
        )
    }
}

// Data class for product information
data class ProductData(
    val id: String,
    val name: String,
    val price: String,
    val image: Int,
    val description: String
)

@Preview(showBackground = true)
@Composable
fun PrdScreenPreview() {
    CafeTrioTheme {
        PrdScreen(productId = "xoai_granola")
    }
} 