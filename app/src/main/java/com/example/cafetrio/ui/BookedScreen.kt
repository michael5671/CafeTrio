package com.example.cafetrio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState

data class CategoryItem(
    val id: String,
    val title: String,
    val imageRes: Int
)

data class Product(
    val id: String,
    val name: String,
    val price: String,
    val imageRes: Int,
    val isNew: Boolean = false
)

data class CollectionBanner(
    val id: String,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookedScreen(
    onBackClick: () -> Unit = {},
    onNavigationItemClick: (String) -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showCategorySheet by remember { mutableStateOf(false) }
    
    // Bottom sheet state
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    // Categories
    val categories = remember {
        listOf(
            CategoryItem("mon_moi", "Món Mới\nPhải Thử", R.drawable.mon_moi_phai_thu),
            CategoryItem("cloud_tea", "Trà Sữa -\nCloudTea", R.drawable.cloud_tea),
            CategoryItem("cloud_fee", "Cà Phê -\nCloudFee", R.drawable.cloud_fee),
            CategoryItem("mon_nong", "Món Nóng", R.drawable.hot_fee),
            CategoryItem("hi_tea", "Trà Trái Cây\n- HiTea", R.drawable.hi_tea),
            CategoryItem("take_away", "Cà Phê -\nTrà Đóng Gói", R.drawable.take_away_fee)
        )
    }
    
    // Refs to sections for scrolling
    val sectionRefs = remember {
        mutableMapOf<String, MutableState<Int>>()
    }
    
    // Initialize section refs
    categories.forEach { category ->
        sectionRefs[category.id] = remember { mutableStateOf(0) }
    }
    
    if (showSearchDialog) {
        SearchDialog(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onDismiss = { 
                showSearchDialog = false
                searchQuery = "" // Reset search query when dialog is dismissed
            }
        )
    }

    // Category Bottom Sheet
    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFFF8F1DF),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                // Header with title and close button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F1DF))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Danh mục sản phẩm",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    
                    IconButton(
                        onClick = { 
                            coroutineScope.launch { 
                                sheetState.hide()
                                showCategorySheet = false 
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

                // Categories grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // First row - 3 items
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        categories.take(3).forEach { category ->
                            CategorySheetItem(
                                category = category,
                                onClick = {
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        showCategorySheet = false
                                        // Scroll to the selected category
                                        sectionRefs[category.id]?.value?.let { position ->
                                            scrollState.animateScrollTo(
                                                value = position,
                                                animationSpec = tween(durationMillis = 1000)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Second row - 3 items
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        categories.takeLast(3).forEach { category ->
                            CategorySheetItem(
                                category = category,
                                onClick = {
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        showCategorySheet = false
                                        // Scroll to the selected category
                                        sectionRefs[category.id]?.value?.let { position ->
                                            scrollState.animateScrollTo(
                                                value = position,
                                                animationSpec = tween(durationMillis = 1000)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    val backgroundColor = Color(0xFFF8F4E1)
    
    // Products for "Món Mới Phải Thử" section
    val newProducts = listOf(
        Product("p1", "Smoothie Xoài Nhiệt Đới Granola", "65.000đ", R.drawable.xoai_granola, true)
    )
    
    // Example products for other sections
    val defaultProducts = listOf(
        Product("p2", "Trà Sữa Oolong Tứ Quý Sương Sáo", "55.000đ", R.drawable.tra_sua_oolong_tu_quy_suong_sao),
        Product("p3", "Cà Phê Sữa Đá", "39.000đ", R.drawable.cfs_da),
        Product("p4", "Chocolate Nóng", "55.000đ", R.drawable.chocolate_nong)
    )
    
    // Collection banners
    val collections = listOf(
        CollectionBanner("b1", R.drawable.bst_1),
        CollectionBanner("b2", R.drawable.bst_2),
        CollectionBanner("b3", R.drawable.bst_3)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Danh mục with dropdown icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showCategorySheet = true }
                    ) {
                        // Logo square dots
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Danh mục",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF553311)
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                         
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color(0xFF553311)
                        )
                    }
                },
                actions = {
                    // Search button
                    Box(
                        modifier = Modifier.padding(end = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .background(
                                    color = Color(0xFFFFFFFF), 
                                    shape = RoundedCornerShape(size = 20.dp)
                                )
                                .clickable { showSearchDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    // Favorites button
                    Box(
                        modifier = Modifier.padding(end = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .background(
                                    color = Color(0xFFFFFFFF), 
                                    shape = RoundedCornerShape(size = 20.dp)
                                )
                                .clickable { onFavoritesClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_love),
                                contentDescription = "Favorites",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = backgroundColor,
                contentColor = CafeBrown
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Trang chủ
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { 
                                onNavigationItemClick("home")
                            }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Trang chủ",
                            color = Color(0xFFAF8F6F),
                            fontSize = 12.sp
                        )
                    }
                    
                    // Đặt hàng (active)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("order") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_booked),
                            contentDescription = "Order",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Đặt hàng",
                            color = Color(0xFF543310),
                            fontSize = 12.sp
                        )
                        // Active indicator
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(32.dp)
                                .height(2.dp)
                                .background(Color(0xFF543310))
                        )
                    }
                    
                    // Ưu đãi
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("rewards") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_voucher),
                            contentDescription = "Rewards",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Ưu đãi",
                            color = Color(0xFFAF8F6F),
                            fontSize = 12.sp
                        )
                    }
                    
                    // Khác
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigationItemClick("differ") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_differ),
                            contentDescription = "More",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Khác",
                            color = Color(0xFFAF8F6F),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .verticalScroll(scrollState)
        ) {
            // Category buttons grid - reorganized to 2 rows
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // First row - 4 items
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Món Mới Phải Thử
                    CategoryButton(
                        category = categories[0],
                        scrollState = scrollState,
                        sectionRefs = sectionRefs,
                        coroutineScope = coroutineScope,
                        animationDuration = 1000 // Slower animation (1 second)
                    )
                    
                    // Trà Sữa - CloudTea
                    CategoryButton(
                        category = categories[1],
                        scrollState = scrollState,
                        sectionRefs = sectionRefs,
                        coroutineScope = coroutineScope,
                        animationDuration = 1000
                    )
                    
                    // Cà Phê - CloudFee
                    CategoryButton(
                        category = categories[2],
                        scrollState = scrollState,
                        sectionRefs = sectionRefs,
                        coroutineScope = coroutineScope,
                        animationDuration = 1000
                    )
                    
                    // Món Nóng
                    CategoryButton(
                        category = categories[3],
                        scrollState = scrollState,
                        sectionRefs = sectionRefs,
                        coroutineScope = coroutineScope,
                        animationDuration = 1000
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Second row - 2 items, aligned to match first row positions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Trà Trái Cây - HiTea (positioned to align with first item in row 1)
                    CategoryButton(
                        category = categories[4],
                        scrollState = scrollState,
                        sectionRefs = sectionRefs,
                        coroutineScope = coroutineScope,
                        animationDuration = 1000
                    )
                    
                    // Cà Phê - Trà Đóng Gói (positioned to align with second item in row 1)
                    CategoryButton(
                        category = categories[5],
                        scrollState = scrollState,
                        sectionRefs = sectionRefs,
                        coroutineScope = coroutineScope,
                        animationDuration = 1000
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Bộ sưu tập section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                Text(
                        text = "Bộ sưu tập",
                    fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Collection banner slider - enlarged banners
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(collections) { collection ->
                            Image(
                                painter = painterResource(id = collection.imageRes),
                                contentDescription = "Collection Banner",
                                modifier = Modifier
                                    .height(200.dp) // Increased height
                                    .fillParentMaxWidth(1f) // Slightly wider
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Món Mới Phải Thử section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        sectionRefs["mon_moi"]?.value = coordinates.positionInParent().y.toInt()
                    }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Món Mới Phải Thử",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    newProducts.forEach { product ->
                        ProductItem(product = product)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Trà Sữa - CloudTea section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        sectionRefs["cloud_tea"]?.value = coordinates.positionInParent().y.toInt()
                    }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Trà Sữa - CloudTea",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    defaultProducts.filter { it.id == "p2" }.forEach { product ->
                        ProductItem(product = product)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Cà Phê - CloudFee section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        sectionRefs["cloud_fee"]?.value = coordinates.positionInParent().y.toInt()
                    }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Cà Phê - CloudFee",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    defaultProducts.filter { it.id == "p3" }.forEach { product ->
                        ProductItem(product = product)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Món Nóng section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        sectionRefs["mon_nong"]?.value = coordinates.positionInParent().y.toInt()
                    }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Món Nóng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    defaultProducts.filter { it.id == "p4" }.forEach { product ->
                        ProductItem(product = product)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Trà Trái Cây - HiTea section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        sectionRefs["hi_tea"]?.value = coordinates.positionInParent().y.toInt()
                    }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Trà Trái Cây - HiTea",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    defaultProducts.take(1).forEach { product ->
                        ProductItem(product = product.copy(name = "Oolong Tứ Quý Dâu Trân Châu", price = "49.000đ", imageRes = R.drawable.oolong_tu_quy_dau))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Cà Phê - Trà Đóng Gói section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        sectionRefs["take_away"]?.value = coordinates.positionInParent().y.toInt()
                    }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Cà Phê - Trà Đóng Gói",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF553311),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    defaultProducts.take(1).forEach { product ->
                        ProductItem(product = product.copy(name = "Cà Phê Đen Đá Hộp (14 gói x 16g)", price = "66.000đ", imageRes = R.drawable.cpg_cf_den_da_hop_14_goi))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CategoryButton(
    category: CategoryItem,
    scrollState: ScrollState,
    sectionRefs: Map<String, MutableState<Int>>,
    coroutineScope: CoroutineScope,
    animationDuration: Int = 300 // Default animation duration in ms
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable {
                coroutineScope.launch {
                    sectionRefs[category.id]?.value?.let { position ->
                        // Calculate scroll distance and use animateScrollBy with slower animation
                        val distance = position - scrollState.value
                        scrollState.animateScrollBy(
                            value = distance.toFloat(),
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    }
                }
            }
    ) {
        // Category icon
        Image(
            painter = painterResource(id = category.imageRes),
            contentDescription = category.title,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Category name
        Text(
            text = category.title,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF553311),
            lineHeight = 14.sp
        )
    }
}

@Composable
fun ProductItem(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle product click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product image
        Box {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            // New tag
            if (product.isNew) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.Red, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "NEW",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Product details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF553311),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = product.price,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF553311)
            )
        }
    }
}

@Composable
fun SearchDialog(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF8F1DF)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tìm kiếm",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF543310)
                    )
                    
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF543310)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search input field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Nhập từ khóa tìm kiếm...",
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search",
                            tint = Color(0xFF543310),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(
                                onClick = { onSearchQueryChange("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = Color.Gray
                                )
                            }
                        }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF543310),
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search suggestions or recent searches
                Column {
                    Text(
                        text = "Tìm kiếm gần đây",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF543310),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Recent search items
                    RecentSearchItem(
                        text = "Cà phê sữa",
                        onClick = {
                            onSearchQueryChange("Cà phê sữa")
                        }
                    )
                    RecentSearchItem(
                        text = "Trà sữa",
                        onClick = {
                            onSearchQueryChange("Trà sữa")
                        }
                    )
                    RecentSearchItem(
                        text = "Smoothie",
                        onClick = {
                            onSearchQueryChange("Smoothie")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecentSearchItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Recent search",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF543310)
        )
    }
}

@Composable
fun CategorySheetItem(
    category: CategoryItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        // Category image
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Category name
        Text(
            text = category.title.replace("\n", " "),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF553311),
            maxLines = 2,
            lineHeight = 18.sp
        )
    }
} 