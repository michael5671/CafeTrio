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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeTrioTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrdScreen(
    productId: String,
    onBackClick: () -> Unit = {}
) {
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
    
    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Close button in top-right
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Đóng",
                        tint = Color.White
                    )
                }
            }
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
                
                // Add to cart button (not visible in the image but keeping it for functionality)
                Button(
                    onClick = { /* TODO: Add to cart logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8D6E63)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Thêm vào giỏ hàng - ${formatPrice(totalPrice)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
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

// Helper function to format the price
fun formatPrice(priceInVND: Int): String {
    return "${priceInVND.toString().chunked(3).joinToString(".")}đ"
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