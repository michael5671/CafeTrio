package com.example.cafetrio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cafetrio.R
import com.example.cafetrio.ui.admin.ProductDetailDialog
import com.example.cafetrio.ui.admin.VoucherDetailDialog
import com.example.cafetrio.ui.theme.*
import com.example.cafetrio.data.api.ApiClient
import com.example.cafetrio.data.dto.ProductCreateRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import com.example.cafetrio.ui.admin.ProductManagementContent

import com.example.cafetrio.ui.admin.DeleteConfirmationDialog



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onLogoutClick: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("DASHBOARD", "SẢN PHẨM", "VOUCHER", "DANH MỤC")
    
    // Dialog states
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }
    var showVoucherDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Café Trio",
                        fontFamily = FontFamily(Font(R.font.agbalumo_regular)),
                        fontSize = 26.sp,
                        color = CafeBrown
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Đăng xuất",
                            tint = CafeBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CafeBeige
                )
            )
        },
        floatingActionButton = {
            if (selectedTabIndex != 0) { // Không hiển thị FAB ở tab Dashboard
                FloatingActionButton(
                    onClick = { 
                        when (selectedTabIndex) {
                            1 -> showProductDialog = true // Tab Sản phẩm
                            2 -> showVoucherDialog = true // Tab Voucher
                            3 -> showCategoryDialog = true // Tab Danh mục
                        }
                    },
                    containerColor = CafeButtonBackground,
                    contentColor = CafeBeige,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm mới"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CafeLoginBackground)
        ) {
            // Tab Bar
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = CafeBeige,
                contentColor = CafeBrown,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 2.dp,
                        color = CafeBrown
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { 
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }
            
            // Nội dung tab
            when (selectedTabIndex) {
                0 -> DashboardContent()
                1 -> ProductManagementContent(
                    showAddDialog = showProductDialog,
                    onAddDialogDismiss = { showProductDialog = false }
                )
                2 -> VoucherManagementContent(
                    showAddDialog = showVoucherDialog,
                    onAddDialogDismiss = { showVoucherDialog = false }
                )
                3 -> CategoryManagementContent(
                    showAddDialog = showCategoryDialog,
                    onAddDialogDismiss = { showCategoryDialog = false },
                    onEditCategory = { id, name ->
                        editingCategory = Pair(id, name)
                        showCategoryDialog = true
                    },
                    onDeleteCategory = { name ->
                        categoryToDelete = name
                        showDeleteCategoryDialog = true
                    }
                )
            }
        }
    }
    
    // Các dialog quản lý danh mục
    if (showCategoryDialog) {
        CategoryDialog(
            isEditing = editingCategory != null,
            categoryName = editingCategory?.second ?: "",
            categoryDescription = "",
            onDismiss = { 
                showCategoryDialog = false
                editingCategory = null
            },
            onSave = { name, description ->
                // Xử lý lưu danh mục
                showCategoryDialog = false
                editingCategory = null
            }
        )
    }
    
    if (showDeleteCategoryDialog) {
        DeleteConfirmationDialog(
            isVisible = true,
            itemName = categoryToDelete,
            onDismiss = { showDeleteCategoryDialog = false },
            onConfirm = {
                // Xử lý xóa danh mục
                showDeleteCategoryDialog = false
            }
        )
    }
}

@Composable
fun DashboardContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tổng quan",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = CafeBrown,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Card row 1
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tổng đơn hàng
            DashboardCard(
                title = "Tổng đơn hàng",
                value = "128",
                backgroundColor = CafeBeige,
                modifier = Modifier.weight(1f)
            )
            
            // Doanh thu
            DashboardCard(
                title = "Doanh thu",
                value = "14.5M VNĐ",
                backgroundColor = Color(0xFFF5E6CC),
                modifier = Modifier.weight(1f)
            )
        }
        
        // Card row 2
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sản phẩm
            DashboardCard(
                title = "Sản phẩm",
                value = "42",
                backgroundColor = Color(0xFFE6CCCC),
                modifier = Modifier.weight(1f)
            )
            
            // Khách hàng
            DashboardCard(
                title = "Khách hàng",
                value = "256",
                backgroundColor = Color(0xFFCCE6D4),
                modifier = Modifier.weight(1f)
            )
        }
        
        // Biểu đồ đơn giản - đơn hàng theo ngày (hiện thị dạng thanh ngang)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(top = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Đơn hàng 7 ngày gần đây",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CafeBrown,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Thanh thống kê theo ngày
                val dailyOrders = listOf(8, 12, 5, 15, 10, 7, 18)
                val maxValue = dailyOrders.maxOrNull() ?: 1
                val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dailyOrders.forEachIndexed { index, count ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Nhãn ngày
                            Text(
                                text = days[index],
                                color = CafeBrown,
                                fontSize = 14.sp,
                                modifier = Modifier.width(30.dp)
                            )
                            
                            // Thanh thống kê
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFE8E8E8))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(count.toFloat() / maxValue)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CafeButtonBackground)
                                ) {}
                            }
                            
                            // Giá trị
                            Text(
                                text = count.toString(),
                                color = CafeBrown,
                                fontSize = 14.sp,
                                modifier = Modifier.width(30.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CafeBrown
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = title,
                fontSize = 14.sp,
                color = CafeBrown.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun VoucherManagementContent(
    showAddDialog: Boolean = false,
    onAddDialogDismiss: () -> Unit = {}
) {
    // Danh sách voucher mẫu để hiển thị
    val voucherList = remember {
        listOf(
            Pair("WELCOME20", "Giảm 20% cho đơn hàng đầu tiên"),
            Pair("SUMMER10", "Giảm 10% cho tất cả đồ uống mùa hè"),
            Pair("FREESHIP", "Miễn phí giao hàng cho đơn từ 200.000đ"),
            Pair("WEEKEND25", "Giảm 25% vào cuối tuần")
        )
    }
    
    var showAddEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedVoucher by remember { mutableStateOf<Pair<String, String>?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    
    // Kích hoạt dialog thêm mới khi FAB được nhấn
    LaunchedEffect(showAddDialog) {
        if (showAddDialog) {
            showAddEditDialog = true
            isEditing = false
            selectedVoucher = null
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(voucherList) { voucher ->
                VoucherItem(
                    code = voucher.first,
                    description = voucher.second,
                    onEditClick = { 
                        selectedVoucher = voucher
                        isEditing = true
                        showAddEditDialog = true
                    },
                    onDeleteClick = { 
                        selectedVoucher = voucher
                        showDeleteDialog = true
                    }
                )
            }
            // Thêm khoảng trống ở cuối để tránh FAB che mất item cuối
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
    
    // Dialog thêm/sửa voucher
    if (showAddEditDialog) {
        VoucherDetailDialog(
            isVisible = true,
            isEditing = isEditing,
            voucherCode = selectedVoucher?.first ?: "",
            voucherDescription = selectedVoucher?.second ?: "",
            voucherDiscount = if (isEditing) "20" else "",  // Giả sử giá trị mặc định
            voucherMinAmount = "50000",  // Giả sử giá trị mặc định
            voucherMaxDiscount = "100000",  // Giả sử giá trị tối đa
            voucherStartDate = "",  // Sẽ dùng ngày hiện tại
            voucherEndDate = "",  // Sẽ dùng ngày hiện tại
            onDismiss = { 
                showAddEditDialog = false 
                if (showAddDialog) onAddDialogDismiss()
            },
            onSave = { code, description, discount, minAmount, maxDiscount, startDate, endDate ->
                // Xử lý lưu voucher
                showAddEditDialog = false
                if (showAddDialog) onAddDialogDismiss()
            }
        )
    }
    
    // Dialog xác nhận xóa
    if (showDeleteDialog && selectedVoucher != null) {
        DeleteConfirmationDialog(
            isVisible = true,
            itemName = selectedVoucher?.first ?: "",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                // Xử lý xóa voucher
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun CategoryManagementContent(
    showAddDialog: Boolean = false,
    onAddDialogDismiss: () -> Unit = {},
    onEditCategory: (String, String) -> Unit,
    onDeleteCategory: (String) -> Unit
) {
    // Danh sách danh mục mẫu
    val categoryList = remember {
        listOf(
            Pair("cat1", "Cà phê"),
            Pair("cat2", "Trà sữa"),
            Pair("cat3", "Nước ép"),
            Pair("cat4", "Sinh tố"),
            Pair("cat5", "Đồ ăn nhẹ")
        )
    }
    
    var showAddEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Pair<String, String>?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    
    // Kích hoạt dialog thêm mới khi FAB được nhấn
    LaunchedEffect(showAddDialog) {
        if (showAddDialog) {
            showAddEditDialog = true
            isEditing = false
            selectedCategory = null
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categoryList) { category ->
                CategoryItem(
                    name = category.second,
                    onEditClick = { 
                        selectedCategory = category
                        isEditing = true
                        showAddEditDialog = true
                    },
                    onDeleteClick = { 
                        selectedCategory = category
                        showDeleteDialog = true
                    }
                )
            }
            // Thêm khoảng trống ở cuối để tránh FAB che mất item cuối
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
    
    // Dialog thêm/sửa danh mục
    if (showAddEditDialog) {
        CategoryDialog(
            isEditing = isEditing,
            categoryName = selectedCategory?.second ?: "",
            categoryDescription = "Mô tả danh mục",  // Giả sử mô tả mặc định
            onDismiss = { 
                showAddEditDialog = false 
                if (showAddDialog) onAddDialogDismiss()
            },
            onSave = { name, description ->
                if (isEditing && selectedCategory != null) {
                    onEditCategory(selectedCategory!!.first, name)
                } else {
                    // Xử lý thêm danh mục mới
                }
                showAddEditDialog = false
                if (showAddDialog) onAddDialogDismiss()
            }
        )
    }
    
    // Dialog xác nhận xóa
    if (showDeleteDialog && selectedCategory != null) {
        DeleteConfirmationDialog(
            isVisible = true,
            itemName = selectedCategory?.second ?: "",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                onDeleteCategory(selectedCategory!!.second)
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun CategoryItem(
    name: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tên danh mục
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                color = CafeBrown,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            
            // Nút chỉnh sửa
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = CafeBeige,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Chỉnh sửa",
                    tint = CafeBrown,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Nút xóa
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryDialog(
    isEditing: Boolean = false,
    categoryName: String = "",
    categoryDescription: String = "",
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(categoryName) }
    var description by remember { mutableStateOf(categoryDescription) }
    
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
            ) {
                Text(
                    text = if (isEditing) "Chỉnh Sửa Danh Mục" else "Thêm Danh Mục Mới",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = CafeBrown
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên danh mục") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CafeBrown,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = CafeBrown
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
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
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CafeBrown
                        )
                    ) {
                        Text("Hủy")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = { 
                            if (name.isNotEmpty()) {
                                onSave(name, description)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CafeButtonBackground
                        )
                    ) {
                        Text(
                            text = if (isEditing) "Cập Nhật" else "Thêm Mới",
                            color = CafeBeige
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    name: String,
    price: String,
    imageRes: Int,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hình ảnh sản phẩm
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CafeBeige)
            ) {
                Icon(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(8.dp),
                    tint = Color.Unspecified
                )
            }
            
            // Thông tin sản phẩm
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    color = CafeBrown,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = price,
                    color = CafeLightBrown,
                    fontSize = 14.sp
                )
            }
            
            // Nút chỉnh sửa
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = CafeBeige,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Chỉnh sửa",
                    tint = CafeBrown,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Nút xóa
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun VoucherItem(
    code: String,
    description: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mã voucher
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CafeButtonBackground.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = CafeButtonBackground,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = code,
                    color = CafeButtonBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            
            // Mô tả voucher
            Text(
                text = description,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                color = CafeBrown,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            // Nút chỉnh sửa
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = CafeBeige,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Chỉnh sửa",
                    tint = CafeBrown,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Nút xóa
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(18.dp)
                )
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