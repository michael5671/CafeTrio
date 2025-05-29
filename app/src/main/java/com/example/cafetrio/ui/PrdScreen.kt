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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrdScreen(
    productId: String,
    onBackClick: () -> Unit = {},
    onViewCart: () -> Unit = {},
    onNavigateToMain: () -> Unit = {}
) {
    // State for showing success dialog
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // Product data for Xoài Granola
    val product = ProductData(
        id = "xoai_granola",
        name = "Smoothie Xoài Nhiệt Đới Granola",
        price = "65.000đ",
        image = R.drawable.xoai_granola,
        description = "Hương vị trái cây tươi mát gọi gọn trong ly Smoothie Xoài Nhiệt Đới Granola. Xoài ngọt đậm quyện cùng sữa chua sánh mịn. Nhân đôi healthy với ngũ cốc Granola và topping hạt nổ sữa chua vui miệng."
    )
    
    // State for wishlist button
    var isInWishlist by remember { mutableStateOf(false) }
    
    // State for size selection (default to Vừa)
    var selectedSize by remember { mutableStateOf("Vừa") }
    
    // State for toppings (multiple selection allowed, max 2)
    val toppingOptions = listOf(
        "Trái Vải", 
        "Hạt Sen", 
        "Thạch Cà Phê", 
        "Trân châu trắng", 
        "Đào Miếng"
    )
    val selectedToppings = remember { mutableStateListOf<String>() }
    
    // State for expandable description
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    
    // State for note text
    var noteText by remember { mutableStateOf("") }
    
    // Calculate total price based on selections
    val basePrice = if (selectedSize == "Vừa") 69000 else 65000
    val toppingsAddition = selectedToppings.size * 10000
    val totalPrice = basePrice + toppingsAddition

    val backgroundColor = Color(0xFFF8F1DF) // Beige color matching the image
    
    // Cart manager to add items
    val cartManager = remember { CartManager.getInstance() }
    
    // Success Dialog
    if (showSuccessDialog) {
        Dialog(
            onDismissRequest = { 
                showSuccessDialog = false
                onNavigateToMain() // Navigate to MainScreen when dialog is dismissed
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
                    // Close button
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
                                    onNavigateToMain() // Navigate to MainScreen when close button is clicked
                                }
                        )
                    }
                    
                    // Dialog content
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
                title = { }, // Empty title
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
        }
    ) { paddingValues ->
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
                Image(
                    painter = painterResource(id = product.image),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Banner "NEW" ở góc trên bên trái
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
                // Product name with wishlist button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037),
                        modifier = Modifier.weight(1f)
                    )
                    
                    IconButton(
                        onClick = { isInWishlist = !isInWishlist },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_love),
                            contentDescription = "Yêu thích",
                            tint = if (isInWishlist) Color.Red else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                
                // Product price
                Text(
                    text = product.price,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D4037)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Product description
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF5D4037)
                )
                
                // Rút gọn (shown only when expanded, but we keep it simple here)
                if (isDescriptionExpanded) {
                    Text(
                        text = "Rút gọn",
                        fontSize = 14.sp,
                        color = Color(0xFF8D6E63),
                        modifier = Modifier.clickable { isDescriptionExpanded = false }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Size selection with required marker
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Size",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF5D4037)
                    )
                    
                    Text(
                        text = "*",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                }
                
                // Chọn 1 loại size
                Text(
                    text = "Chọn 1 loại size",
                    fontSize = 14.sp,
                    color = Color(0xFF8D6E63),
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                
                // Size options
                SizeOption(
                    size = "Vừa",
                    price = "69.000đ",
                    isSelected = selectedSize == "Vừa",
                    onSelect = { selectedSize = "Vừa" }
                )
                
                SizeOption(
                    size = "Nhỏ",
                    price = "65.000đ",
                    isSelected = selectedSize == "Nhỏ",
                    onSelect = { selectedSize = "Nhỏ" }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Toppings selection
                Text(
                    text = "Topping",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF5D4037)
                )
                
                // Chọn tối đa 2 loại
                Text(
                    text = "Chọn tối đa 2 loại",
                    fontSize = 14.sp,
                    color = Color(0xFF8D6E63),
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                
                // Topping options
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    toppingOptions.forEach { topping ->
                        ToppingOption(
                            name = topping,
                            price = "10.000đ",
                            isSelected = selectedToppings.contains(topping),
                            onToggle = {
                                if (selectedToppings.contains(topping)) {
                                    selectedToppings.remove(topping)
                                } else if (selectedToppings.size < 2) {
                                    selectedToppings.add(topping)
                                }
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Yêu cầu khác
                Text(
                    text = "Yêu cầu khác",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF5D4037)
                )
                
                // Những tùy chọn khác
                Text(
                    text = "Những tùy chọn khác",
                    fontSize = 14.sp,
                    color = Color(0xFF8D6E63),
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                
                // Note input
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    placeholder = { Text("Thêm ghi chú") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFBDBDBD),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Quantity selector and add to cart button
                var quantity by remember { mutableStateOf(1) }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus button
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
                    
                    // Quantity display
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
                    
                    // Plus button
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
                    
                    // Price button
                    Button(
                        onClick = { 
                            // Add to cart with selected options
                            val cartItem = CartItem(
                                id = UUID.randomUUID().toString(),
                                productName = product.name,
                                size = selectedSize,
                                quantity = quantity,
                                price = totalPrice,
                                toppings = selectedToppings.toList(),
                                note = noteText
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
                            text = FormatUtils.formatPrice(totalPrice * quantity),
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