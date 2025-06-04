package com.example.cafetrio.ui.admin

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import coil.compose.AsyncImage
import com.example.cafetrio.ui.theme.*
import com.example.cafetrio.data.api.ApiClient
import com.example.cafetrio.data.dto.CategoryResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import org.json.JSONArray

@Composable
fun ProductDetailDialog(
    isVisible: Boolean,
    isEditing: Boolean = false,
    productName: String = "",
    productPrice: String = "",
    productDescription: String = "",
    productCategory: String = "",
    productImageUrl: String = "",
    onDismiss: () -> Unit,
    onSave: (String, String, String, Int, String) -> Unit
) {
    if (!isVisible) return
    
    var name by remember { mutableStateOf(productName) }
    var price by remember { mutableStateOf(productPrice) }
    var description by remember { mutableStateOf(productDescription) }
    var imageUrl by remember { mutableStateOf(productImageUrl) }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var categories by remember { mutableStateOf<List<Pair<Int, String>>>(emptyList()) }
    var isLoadingCategories by remember { mutableStateOf(true) }
    var categoryError by remember { mutableStateOf<String?>(null) }

    android.util.Log.d("ProductDetailDialog", "Dialog hiển thị: $isVisible, IsEditing: $isEditing, Initial Category: $productCategory")

    LaunchedEffect(isVisible) {
        if (isVisible) {
            if (isEditing) {
                val parsedId = productCategory.toIntOrNull()
                selectedCategoryId = parsedId
                android.util.Log.d("ProductDetailDialog", "Parsed Category ID from product: $productCategory -> $parsedId")
            }
            isLoadingCategories = true
            categoryError = null
            android.util.Log.d("ProductDetailDialog", "Bắt đầu fetch categories")
            ApiClient.apiService.getCategories().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    isLoadingCategories = false
                    android.util.Log.d("ProductDetailDialog", "Fetch categories response: ${response.code()}")
                    if (response.isSuccessful) {
                        try {
                            val body = response.body()?.string()
                            val json = JSONObject(body)
                            val dataArr = json.getJSONArray("data")
                            val cats = mutableListOf<Pair<Int, String>>()
                            for (i in 0 until dataArr.length()) {
                                val obj = dataArr.getJSONObject(i)
                                cats.add(obj.getInt("id") to obj.getString("name"))
                            }
                            categories = cats
                            android.util.Log.d("ProductDetailDialog", "Fetched ${cats.size} categories.")
                            if (isEditing && selectedCategoryId != null) {
                                val selectedCatName = categories.firstOrNull { it.first == selectedCategoryId }?.second
                                android.util.Log.d("ProductDetailDialog", "After fetch, selectedCategoryId: $selectedCategoryId, found name: $selectedCatName")
                            }
                        } catch (e: Exception) {
                            categoryError = "Lỗi parse dữ liệu: ${e.message}"
                            android.util.Log.e("ProductDetailDialog", "Error parsing categories: ${e.message}")
                        }
                    } else {
                        categoryError = "Lỗi lấy danh mục: ${response.code()}"
                        android.util.Log.e("ProductDetailDialog", "Error fetching categories: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    isLoadingCategories = false
                    categoryError = "Lỗi kết nối: ${t.message}"
                    android.util.Log.e("ProductDetailDialog", "Categories network error: ${t.message}")
                }
            })
        }
    }
    
    val displayedCategoryText = remember(selectedCategoryId, categories) {
        val text = categories.firstOrNull { it.first == selectedCategoryId }?.second ?: "Chọn danh mục"
        android.util.Log.d("ProductDetailDialog", "Displayed category text calculated: $text (selectedId: $selectedCategoryId, categories size: ${categories.size})")
        text
    }

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
                
                // Trường nhập imageUrl
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CafeBrown,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = CafeBrown
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Dropdown chọn danh mục
                if (isLoadingCategories) {
                    Text("Đang tải danh mục...")
                } else if (categoryError != null) {
                    Text(categoryError!!, color = Color.Red)
                } else {
                    Box {
                        OutlinedButton(onClick = { isDropdownExpanded = true }) {
                            Text(displayedCategoryText)
                        }
                        DropdownMenu(expanded = isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
                            categories.forEach { (id, name) ->
                                DropdownMenuItem(onClick = {
                                    selectedCategoryId = id
                                    isDropdownExpanded = false
                                }, text = { Text(name) })
                            }
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
                        val catId = selectedCategoryId
                        if (name.isNotEmpty() && price.isNotEmpty() && catId != null && imageUrl.isNotEmpty()) {
                            onSave(name, price, description, catId, imageUrl)
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

@Composable
fun ProductManagementContent(
    showAddDialog: Boolean = false,
    onAddDialogDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    var products by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoadingProducts by remember { mutableStateOf(true) }
    var productError by remember { mutableStateOf<String?>(null) }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Map<String, Any>?>(null) }
    var selectedProductCategoryId by remember { mutableStateOf<Int?>(null) }
    var isFetchingProductDetail by remember { mutableStateOf(false) }

    // Kích hoạt dialog thêm mới khi FAB (nút dấu cộng) được nhấn (showAddDialog = true)
    LaunchedEffect(showAddDialog) {
        if (showAddDialog) {
            showAddEditDialog = true
            selectedProduct = null
            selectedProductCategoryId = null
        }
    }

    // Fetch products when screen is shown
    LaunchedEffect(Unit) {
        Toast.makeText(context, "ProductManagementContent mounted", Toast.LENGTH_SHORT).show()
        isLoadingProducts = true
        productError = null

        val params = org.json.JSONObject()
        val pageable = org.json.JSONObject()
        pageable.put("page", 0)
        pageable.put("size", 60)
        val sortArr = org.json.JSONArray()
        sortArr.put("name")
        pageable.put("sort", sortArr)
        android.util.Log.d("ProductManagement", "Bắt đầu gọi API lấy sản phẩm với params: ${params} pageable: ${pageable}")
        ApiClient.apiService.getProducts(params.toString(), pageable.toString()).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                isLoadingProducts = false
                android.util.Log.d("ProductManagement", "Nhận response từ API: ${response.code()}")
                if (response.isSuccessful) {
                    try {
                        val body = response.body()?.string()
                        android.util.Log.d("ProductManagement", "Response body: $body")
                        val json = org.json.JSONObject(body)
                        val dataObj = json.getJSONObject("data")
                        val itemsArr = dataObj.getJSONArray("items")
                        val list = mutableListOf<Map<String, Any>>()
                        for (i in 0 until itemsArr.length()) {
                            val item = itemsArr.getJSONObject(i)
                            val map = mutableMapOf<String, Any>()
                            for (key in item.keys()) {
                                map[key] = item.get(key)
                            }
                            list.add(map)
                        }
                        products = list
                        android.util.Log.d("ProductManagement", "Đã parse được ${list.size} sản phẩm")
                        Toast.makeText(context, "Đã lấy ${list.size} sản phẩm", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        android.util.Log.e("ProductManagement", "Lỗi parse JSON: ${e.message}")
                        productError = "Lỗi parse dữ liệu sản phẩm: ${e.message}"
                        Toast.makeText(context, "Lỗi parse dữ liệu sản phẩm", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    android.util.Log.e("ProductManagement", "API trả về lỗi: ${response.code()}")
                    productError = "Lỗi lấy sản phẩm: ${response.code()}"
                    Toast.makeText(context, "Lỗi lấy sản phẩm: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                isLoadingProducts = false
                android.util.Log.e("ProductManagement", "Lỗi kết nối: ${t.message}")
                productError = "Lỗi kết nối: ${t.localizedMessage}"
                Toast.makeText(context, "Lỗi kết nối: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header với tiêu đề (KHÔNG còn nút Thêm sản phẩm mới)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quản lý sản phẩm",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CafeBrown
            )
        }

        // Hiển thị danh sách sản phẩm
        if (isLoadingProducts) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (productError != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(productError!!, color = Color.Red)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedProduct = product
                                val productId = product["id"]?.toString() ?: return@clickable
                                // Fetch product detail to get categoryId
                                isFetchingProductDetail = true
                                ApiClient.apiService.getProductDetail(productId).enqueue(object : retrofit2.Callback<com.example.cafetrio.data.dto.ProductDetailResponse> {
                                    override fun onResponse(call: retrofit2.Call<com.example.cafetrio.data.dto.ProductDetailResponse>, response: retrofit2.Response<com.example.cafetrio.data.dto.ProductDetailResponse>) {
                                        isFetchingProductDetail = false
                                        if (response.isSuccessful) {
                                            val detailResponse = response.body()
                                            val categoryId = detailResponse?.data?.categoryId
                                            selectedProductCategoryId = categoryId // Lưu categoryId
                                            showAddEditDialog = true // Mở dialog sau khi có categoryId
                                        } else {
                                            Toast.makeText(context, "Lỗi lấy chi tiết sản phẩm: ${response.code()}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onFailure(call: retrofit2.Call<com.example.cafetrio.data.dto.ProductDetailResponse>, t: Throwable) {
                                        isFetchingProductDetail = false
                                        Toast.makeText(context, "Lỗi kết nối lấy chi tiết sản phẩm", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val imageUrl = product["imageUrl"]?.toString() ?: ""
                            if (imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                                Text(product["name"]?.toString() ?: "", fontWeight = FontWeight.Bold)
                                Text(product["description"]?.toString() ?: "")
                                Text(product["price"]?.toString() ?: "", color = Color.Gray)
                            }
                            // Nút update (cây bút)
                            IconButton(
                                onClick = {
                                    selectedProduct = product
                                    showAddEditDialog = true
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Chỉnh sửa",
                                    tint = CafeBrown,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            // Nút xóa
                            IconButton(
                                onClick = {
                                    selectedProduct = product
                                    showDeleteDialog = true
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Xóa",
                                    tint = Color(0xFFE57373),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog thêm/sửa sản phẩm
    if (showAddEditDialog) {
        ProductDetailDialog(
            isVisible = true,
            isEditing = selectedProduct != null,
            productName = selectedProduct?.get("name")?.toString() ?: "",
            productPrice = selectedProduct?.get("price")?.toString() ?: "",
            productDescription = selectedProduct?.get("description")?.toString() ?: "",
            productCategory = selectedProductCategoryId?.toString() ?: "",
            productImageUrl = selectedProduct?.get("imageUrl")?.toString() ?: "",
            onDismiss = {
                showAddEditDialog = false
                selectedProduct = null
                selectedProductCategoryId = null
            },
            onSave = { name, price, description, categoryId, imageUrl ->
                val req = com.example.cafetrio.data.dto.ProductCreateRequest(
                    name = name,
                    description = description,
                    price = price.toIntOrNull() ?: 0,
                    imageUrl = imageUrl,
                    categoryId = categoryId
                )
                val id = selectedProduct?.get("id")?.toString()
                if (id != null) {
                    // Update sản phẩm
                    ApiClient.apiService.updateProduct(id, req).enqueue(object : retrofit2.Callback<com.example.cafetrio.data.dto.ProductDetailResponse> {
                        override fun onResponse(call: retrofit2.Call<com.example.cafetrio.data.dto.ProductDetailResponse>, response: retrofit2.Response<com.example.cafetrio.data.dto.ProductDetailResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show()
                                // Reload lại danh sách sản phẩm
                                isLoadingProducts = true
                                productError = null
                                val params = org.json.JSONObject()
                                val pageable = org.json.JSONObject()
                                pageable.put("page", 0)
                                pageable.put("size", 60)
                                val sortArr = org.json.JSONArray()
                                sortArr.put("name")
                                pageable.put("sort", sortArr)
                                ApiClient.apiService.getProducts(params.toString(), pageable.toString()).enqueue(object : retrofit2.Callback<ResponseBody> {
                                    override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                                        isLoadingProducts = false
                                        if (response.isSuccessful) {
                                            try {
                                                val body = response.body()?.string()
                                                val json = org.json.JSONObject(body)
                                                val dataObj = json.getJSONObject("data")
                                                val itemsArr = dataObj.getJSONArray("items")
                                                val list = mutableListOf<Map<String, Any>>()
                                                for (i in 0 until itemsArr.length()) {
                                                    val item = itemsArr.getJSONObject(i)
                                                    val map = mutableMapOf<String, Any>()
                                                    for (key in item.keys()) {
                                                        map[key] = item.get(key)
                                                    }
                                                    list.add(map)
                                                }
                                                products = list
                                            } catch (e: Exception) {
                                                productError = "Lỗi parse dữ liệu sản phẩm: ${e.message}"
                                            }
                                        } else {
                                            productError = "Lỗi lấy sản phẩm: ${response.code()}"
                                        }
                                    }
                                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                                        isLoadingProducts = false
                                        productError = "Lỗi kết nối: ${t.localizedMessage}"
                                    }
                                })
                            } else {
                                Toast.makeText(context, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show()
                            }
                            showAddEditDialog = false
                            selectedProduct = null
                        }
                        override fun onFailure(call: retrofit2.Call<com.example.cafetrio.data.dto.ProductDetailResponse>, t: Throwable) {
                            Toast.makeText(context, "Lỗi kết nối khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show()
                            showAddEditDialog = false
                            selectedProduct = null
                        }
                    })
                } else {
                    // Thêm mới sản phẩm
                    ApiClient.apiService.createProduct(req).enqueue(object : retrofit2.Callback<com.example.cafetrio.data.dto.ProductDetailResponse> {
                        override fun onResponse(call: retrofit2.Call<com.example.cafetrio.data.dto.ProductDetailResponse>, response: retrofit2.Response<com.example.cafetrio.data.dto.ProductDetailResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                // Reload lại danh sách sản phẩm
                                isLoadingProducts = true;
                                productError = null;
                                val params = org.json.JSONObject();
                                val pageable = org.json.JSONObject();
                                pageable.put("page", 0);
                                pageable.put("size", 60);
                                val sortArr = org.json.JSONArray();
                                sortArr.put("name");
                                pageable.put("sort", sortArr);
                                ApiClient.apiService.getProducts(params.toString(), pageable.toString()).enqueue(object : retrofit2.Callback<ResponseBody> {
                                    override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                                        isLoadingProducts = false;
                                        if (response.isSuccessful) {
                                            try {
                                                val body = response.body()?.string();
                                                val json = org.json.JSONObject(body);
                                                val dataObj = json.getJSONObject("data");
                                                val itemsArr = dataObj.getJSONArray("items");
                                                val list = mutableListOf<Map<String, Any>>();
                                                for (i in 0 until itemsArr.length()) {
                                                    val item = itemsArr.getJSONObject(i);
                                                    val map = mutableMapOf<String, Any>();
                                                    for (key in item.keys()) {
                                                        map[key] = item.get(key);
                                                    }
                                                    list.add(map);
                                                }
                                                products = list;
                                            } catch (e: Exception) {
                                                productError = "Lỗi parse dữ liệu sản phẩm: ${e.message}";
                                            }
                                        } else {
                                            productError = "Lỗi lấy sản phẩm: ${response.code()}";
                                        }
                                    }
                                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                                        isLoadingProducts = false;
                                        productError = "Lỗi kết nối: ${t.localizedMessage}";
                                    }
                                });
                            } else {
                                Toast.makeText(context, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                            showAddEditDialog = false;
                            selectedProduct = null;
                        }
                        override fun onFailure(call: retrofit2.Call<com.example.cafetrio.data.dto.ProductDetailResponse>, t: Throwable) {
                            Toast.makeText(context, "Lỗi kết nối khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                            showAddEditDialog = false;
                            selectedProduct = null;
                        }
                    });
                }
            }
        )
    }

    // Dialog xác nhận xóa sản phẩm
    if (showDeleteDialog && selectedProduct != null) {
        DeleteConfirmationDialog(
            isVisible = true,
            itemName = selectedProduct?.get("name")?.toString() ?: "",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                // Gọi API xóa sản phẩm
                val id = selectedProduct?.get("id")?.toString() ?: return@DeleteConfirmationDialog
                ApiClient.apiService.deleteProduct(id).enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Đã xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show()
                            // Reload lại danh sách sản phẩm
                            isLoadingProducts = true
                            productError = null
                            val params = org.json.JSONObject()
                            val pageable = org.json.JSONObject()
                            pageable.put("page", 0)
                            pageable.put("size", 60)
                            val sortArr = org.json.JSONArray()
                            sortArr.put("name")
                            pageable.put("sort", sortArr)
                            ApiClient.apiService.getProducts(params.toString(), pageable.toString()).enqueue(object : retrofit2.Callback<ResponseBody> {
                                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                                    isLoadingProducts = false
                                    if (response.isSuccessful) {
                                        try {
                                            val body = response.body()?.string()
                                            val json = org.json.JSONObject(body)
                                            val dataObj = json.getJSONObject("data")
                                            val itemsArr = dataObj.getJSONArray("items")
                                            val list = mutableListOf<Map<String, Any>>()
                                            for (i in 0 until itemsArr.length()) {
                                                val item = itemsArr.getJSONObject(i)
                                                val map = mutableMapOf<String, Any>()
                                                for (key in item.keys()) {
                                                    map[key] = item.get(key)
                                                }
                                                list.add(map)
                                            }
                                            products = list
                                        } catch (e: Exception) {
                                            productError = "Lỗi parse dữ liệu sản phẩm: ${e.message}"
                                        }
                                    } else {
                                        productError = "Lỗi lấy sản phẩm: ${response.code()}"
                                    }
                                }
                                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                                    isLoadingProducts = false
                                    productError = "Lỗi kết nối: ${t.localizedMessage}"
                                }
                            })
                        } else {
                            Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show()
                        }
                        showDeleteDialog = false
                        selectedProduct = null
                    }
                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(context, "Lỗi kết nối khi xóa sản phẩm", Toast.LENGTH_SHORT).show()
                        showDeleteDialog = false
                        selectedProduct = null
                    }
                })
            }
        )
    }

    // Loading indicator khi fetch detail
    if (isFetchingProductDetail) {
        Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
} 