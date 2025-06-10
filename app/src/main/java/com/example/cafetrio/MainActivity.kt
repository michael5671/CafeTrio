package com.example.cafetrio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cafetrio.data.AuthManager
import com.example.cafetrio.data.dto.OrderDetail
import com.example.cafetrio.ui.*
import com.example.cafetrio.ui.theme.CafeTrioTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Xử lý deeplink khi khởi động
        handleDeepLink(intent)

        setContent {
            CafeTrioTheme {
                var showSplash by remember { mutableStateOf(true) }
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                var previousScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                var emailForOtp by remember { mutableStateOf("") }
                var otpToken by remember { mutableStateOf("") }
                var productId by remember { mutableStateOf("") }
                var selectedOrder: OrderDetail? by remember { mutableStateOf(null) }
                var paymentStatus by remember { mutableStateOf<String?>(null) } // Lưu trạng thái thanh toán
                var transactionId by remember { mutableStateOf<String?>(null) } // Lưu ID giao dịch

                // Get AuthManager instance
                val authManager = remember { AuthManager.getInstance(this@MainActivity) }

                // Hàm cập nhật currentScreen từ deeplink
                fun updateScreenFromDeepLink(uri: Uri) {
                    if (uri.scheme == "yourapp" && uri.host == "payment" && uri.path?.startsWith("/callback") == true) {
                        val status = uri.getQueryParameter("status")
                        val transId = uri.getQueryParameter("transaction_id")
                        Log.d("Deeplink", "Status: $status, Transaction ID: $transId")
                        paymentStatus = status
                        transactionId = transId
                        currentScreen = Screen.Main // Điều hướng đến màn hình chính
                    }
                }

                // Xử lý deeplink ban đầu
                intent.data?.let { uri ->
                    updateScreenFromDeepLink(uri)
                }

                AnimatedContent(
                    targetState = showSplash,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(800)) togetherWith
                                fadeOut(animationSpec = tween(800))
                    },
                    label = "splash_transition"
                ) { isSplashVisible ->
                    if (isSplashVisible) {
                        SplashScreen(onSplashFinished = { showSplash = false })
                    } else {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                when {
                                    targetState == Screen.Main && initialState == Screen.Login -> {
                                        (slideInHorizontally(animationSpec = tween(900)) { width -> width } +
                                                fadeIn(animationSpec = tween(900))) togetherWith
                                                (slideOutHorizontally(animationSpec = tween(900)) { width -> -width } +
                                                        fadeOut(animationSpec = tween(900)))
                                    }
                                    initialState == Screen.Login && targetState == Screen.Main -> {
                                        fadeIn(animationSpec = tween(800)) togetherWith
                                                fadeOut(animationSpec = tween(800))
                                    }
                                    targetState == Screen.Main && initialState == Screen.ProductDetail -> {
                                        fadeIn(animationSpec = tween(150)) togetherWith
                                                fadeOut(animationSpec = tween(150))
                                    }
                                    else -> {
                                        fadeIn(animationSpec = tween(500)) togetherWith
                                                fadeOut(animationSpec = tween(500))
                                    }
                                }
                            },
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                Screen.Login -> LoginScreen(
                                    onForgotPasswordClick = { currentScreen = Screen.ForgotPassword },
                                    onSignUpClick = { currentScreen = Screen.SignUp },
                                    onLoginClick = {
                                        if (authManager.getSavedEmail() == "gm.giaphu@gmail.com") {
                                            currentScreen = Screen.Admin
                                        } else {
                                            currentScreen = Screen.Main
                                        }
                                    }
                                )
                                Screen.ForgotPassword -> ForgotPasswordScreen(
                                    onBackToLogin = { currentScreen = Screen.Login },
                                    onSubmitEmail = { email ->
                                        emailForOtp = email
                                        currentScreen = Screen.OtpVerification
                                    }
                                )
                                Screen.OtpVerification -> OTP_FGPassScreen(
                                    emailAddress = emailForOtp,
                                    onBackClick = { currentScreen = Screen.ForgotPassword },
                                    onVerifyOtp = { otp ->
                                        otpToken = otp
                                        currentScreen = Screen.ChangePassword
                                    }
                                )
                                Screen.ChangePassword -> ChangePasswordScreen(
                                    email = emailForOtp,
                                    onBackClick = { currentScreen = Screen.OtpVerification },
                                    onChangePasswordSubmit = {
                                        currentScreen = Screen.Login
                                    }
                                )
                                Screen.SignUp -> SignUpScreen(
                                    onBackClick = { currentScreen = Screen.Login },
                                    onSignUpSubmit = { email ->
                                        emailForOtp = email
                                        currentScreen = Screen.OtpSignUp
                                    },
                                    onNavigateToOTP = { email ->
                                        emailForOtp = email
                                        currentScreen = Screen.OtpSignUp
                                    }
                                )
                                Screen.OtpSignUp -> OTP_SignUpScreen(
                                    emailAddress = emailForOtp,
                                    onBackClick = { currentScreen = Screen.SignUp },
                                    onVerifyOtp = { otp ->
                                        currentScreen = Screen.Login
                                    }
                                )
                                Screen.Admin -> AdminScreen(
                                    onLogoutClick = {
                                        currentScreen = Screen.Login
                                    }
                                )
                                Screen.Main -> MainScreen(
                                    onNotificationClick = { /* TODO */ },
                                    onMenuClick = { /* TODO */ },
                                    onNavigate = { destination ->
                                        when {
                                            destination == "differ" -> currentScreen = Screen.Differ
                                            destination == "order" -> currentScreen = Screen.Booked
                                            destination == "orders" -> currentScreen = Screen.Cart
                                            destination == "rewards" -> currentScreen = Screen.Coupon
                                            destination.startsWith("product/") -> {
                                                productId = destination.removePrefix("product/")
                                                currentScreen = Screen.ProductDetail
                                            }
                                            else -> { /* Handle other navigation */ }
                                        }
                                    },
                                    onNavigateToNoti = {
                                        previousScreen = Screen.Main
                                        currentScreen = Screen.Noti
                                    },
                                )
                                Screen.ProductDetail -> {
                                    val productDetails = when (productId) {
                                        "xoai_granola" -> Triple(
                                            "Smoothie Xoài Nhiệt Đới Granola",
                                            "65.000đ",
                                            R.drawable.xoai_granola
                                        )
                                        "phuc_bon_tu_granola" -> Triple(
                                            "Smoothie Phúc Bồn Tử Granola",
                                            "65.000đ",
                                            R.drawable.phuc_bon_tu_granola
                                        )
                                        "oolong_tu_quy_vai" -> Triple(
                                            "Oolong Tứ Quý Vải",
                                            "59.000đ",
                                            R.drawable.oolong_tu_quy_vai
                                        )
                                        "oolong_kim_quat_tran_chau" -> Triple(
                                            "Oolong Tứ Quý Kim Quất Trân Châu",
                                            "59.000đ",
                                            R.drawable.oolong_kim_quat_tran_chau
                                        )
                                        "tra_sua_oolong_tu_quy_suong_sao" -> Triple(
                                            "Trà Sữa Oolong Tứ Quý Sương Sáo",
                                            "55.000đ",
                                            R.drawable.tra_sua_oolong_tu_quy_suong_sao
                                        )
                                        else -> Triple("Sản phẩm", "0đ", R.drawable.xoai_granola)
                                    }
                                    PrdScreen(
                                        productId = productId,
                                        onBackClick = { currentScreen = Screen.Main },
                                        onViewCart = { currentScreen = Screen.Cart },
                                        onNavigateToMain = {
                                            currentScreen = Screen.Main
                                        }
                                    )
                                }
                                Screen.Differ -> DifferScreen(
                                    onBackClick = { currentScreen = Screen.Main },
                                    onNavigationItemClick = { destination ->
                                        when (destination) {
                                            "home" -> currentScreen = Screen.Main
                                            "order" -> currentScreen = Screen.Booked
                                            "rewards" -> currentScreen = Screen.Coupon
                                            "user_info" -> currentScreen = Screen.UserInfo
                                            else -> currentScreen = Screen.Differ
                                        }
                                    },
                                    onLogoutClick = {
                                        authManager.clearLoginCredentials()
                                        currentScreen = Screen.Login
                                    },
                                    onHistoryClick = {
                                        currentScreen = Screen.History
                                    },
                                    onNavigateToNoti = {
                                        previousScreen = Screen.Differ
                                        currentScreen = Screen.Noti
                                    }
                                )
                                Screen.UserInfo -> UserInfScreen(
                                    onBackClick = { currentScreen = Screen.Differ }
                                )
                                Screen.Cart -> CartScreen(
                                    onBackClick = { currentScreen = Screen.Main },
                                    onNavigateToPayment = { order ->
                                        selectedOrder = order
                                        currentScreen = Screen.Payment
                                    }
                                )
                                Screen.Payment -> {
                                    selectedOrder?.let { order ->
                                        PaymentScreen(
                                            order = order,
                                            onBackClick = { currentScreen = Screen.Cart },
                                            onNavigateToMain = {
                                                currentScreen = Screen.Main
                                            },
                                            onSelectVoucher = {
                                                currentScreen = Screen.SelectVoucher
                                            }
                                        )
                                    } ?: run {
                                        currentScreen = Screen.Cart
                                    }
                                }
                                Screen.SelectVoucher -> SelectVoucherScreen(
                                    onBackClick = { currentScreen = Screen.Payment },
                                    onVoucherSelect = { voucher ->
                                        currentScreen = Screen.Payment
                                    }
                                )
                                Screen.Coupon -> CouponScreen(
                                    onBackClick = { currentScreen = Screen.Main },
                                    onNavigationItemClick = { destination ->
                                        when (destination) {
                                            "home" -> currentScreen = Screen.Main
                                            "order" -> currentScreen = Screen.Booked
                                            "rewards" -> currentScreen = Screen.Coupon
                                            "differ" -> currentScreen = Screen.Differ
                                            else -> { /* Handle other navigation */ }
                                        }
                                    }
                                )
                                Screen.Booked -> BookedScreen(
                                    onBackClick = { currentScreen = Screen.Main },
                                    onNavigationItemClick = { destination ->
                                        when (destination) {
                                            "home" -> currentScreen = Screen.Main
                                            "order" -> currentScreen = Screen.Booked
                                            "rewards" -> currentScreen = Screen.Coupon
                                            "differ" -> currentScreen = Screen.Differ
                                            else -> { /* Handle other navigation */ }
                                        }
                                    },
                                    onFavoritesClick = {
                                        currentScreen = Screen.WishList
                                    }
                                )
                                Screen.History -> HistoryScreen(
                                    onBackClick = { currentScreen = Screen.Differ }
                                )
                                Screen.WishList -> WishListScreen(
                                    onBackClick = { currentScreen = Screen.Booked }
                                )
                                Screen.Noti -> NotiScreen(
                                    onBackClick = { currentScreen = previousScreen }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Xử lý deeplink khi ứng dụng đã chạy
        setIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.let {
            if (Intent.ACTION_VIEW == intent.action) {
                intent.data?.let { uri ->
                    if (uri.scheme == "yourapp" && uri.host == "payment" && uri.path?.startsWith("/callback") == true) {
                        val status = uri.getQueryParameter("status")
                        val transactionId = uri.getQueryParameter("transaction_id")
                        Log.d("Deeplink", "Received deeplink: $uri")
                        Log.d("Deeplink", "Status: $status, Transaction ID: $transactionId")
                        // Cập nhật setContent để phản ánh deeplink
                        setContent {
                            CafeTrioTheme {
                                var showSplash by remember { mutableStateOf(false) }
                                var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
                                var previousScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                                var emailForOtp by remember { mutableStateOf("") }
                                var otpToken by remember { mutableStateOf("") }
                                var productId by remember { mutableStateOf("") }
                                var selectedOrder: OrderDetail? by remember { mutableStateOf(null) }
                                var paymentStatus by remember { mutableStateOf(status) }
                                var transactionId by remember { mutableStateOf(transactionId) }
                                val authManager = remember { AuthManager.getInstance(this@MainActivity) }

                                AnimatedContent(
                                    targetState = showSplash,
                                    transitionSpec = {
                                        fadeIn(animationSpec = tween(800)) togetherWith
                                                fadeOut(animationSpec = tween(800))
                                    },
                                    label = "splash_transition"
                                ) { isSplashVisible ->
                                    if (isSplashVisible) {
                                        SplashScreen(onSplashFinished = { showSplash = false })
                                    } else {
                                        AnimatedContent(
                                            targetState = currentScreen,
                                            transitionSpec = {
                                                when {
                                                    targetState == Screen.Main && initialState == Screen.Login -> {
                                                        (slideInHorizontally(animationSpec = tween(900)) { width -> width } +
                                                                fadeIn(animationSpec = tween(900))) togetherWith
                                                                (slideOutHorizontally(animationSpec = tween(900)) { width -> -width } +
                                                                        fadeOut(animationSpec = tween(900)))
                                                    }
                                                    initialState == Screen.Login && targetState == Screen.Main -> {
                                                        fadeIn(animationSpec = tween(800)) togetherWith
                                                                fadeOut(animationSpec = tween(800))
                                                    }
                                                    targetState == Screen.Main && initialState == Screen.ProductDetail -> {
                                                        fadeIn(animationSpec = tween(150)) togetherWith
                                                                fadeOut(animationSpec = tween(150))
                                                    }
                                                    else -> {
                                                        fadeIn(animationSpec = tween(500)) togetherWith
                                                                fadeOut(animationSpec = tween(500))
                                                    }
                                                }
                                            },
                                            label = "screen_transition"
                                        ) { screen ->
                                            when (screen) {
                                                Screen.Login -> LoginScreen(
                                                    onForgotPasswordClick = { currentScreen = Screen.ForgotPassword },
                                                    onSignUpClick = { currentScreen = Screen.SignUp },
                                                    onLoginClick = {
                                                        if (authManager.getSavedEmail() == "gm.giaphu@gmail.com") {
                                                            currentScreen = Screen.Admin
                                                        } else {
                                                            currentScreen = Screen.Main
                                                        }
                                                    }
                                                )
                                                Screen.ForgotPassword -> ForgotPasswordScreen(
                                                    onBackToLogin = { currentScreen = Screen.Login },
                                                    onSubmitEmail = { email ->
                                                        emailForOtp = email
                                                        currentScreen = Screen.OtpVerification
                                                    }
                                                )
                                                Screen.OtpVerification -> OTP_FGPassScreen(
                                                    emailAddress = emailForOtp,
                                                    onBackClick = { currentScreen = Screen.ForgotPassword },
                                                    onVerifyOtp = { otp ->
                                                        otpToken = otp
                                                        currentScreen = Screen.ChangePassword
                                                    }
                                                )
                                                Screen.ChangePassword -> ChangePasswordScreen(
                                                    email = emailForOtp,
                                                    onBackClick = { currentScreen = Screen.OtpVerification },
                                                    onChangePasswordSubmit = {
                                                        currentScreen = Screen.Login
                                                    }
                                                )
                                                Screen.SignUp -> SignUpScreen(
                                                    onBackClick = { currentScreen = Screen.Login },
                                                    onSignUpSubmit = { email ->
                                                        emailForOtp = email
                                                        currentScreen = Screen.OtpSignUp
                                                    },
                                                    onNavigateToOTP = { email ->
                                                        emailForOtp = email
                                                        currentScreen = Screen.OtpSignUp
                                                    }
                                                )
                                                Screen.OtpSignUp -> OTP_SignUpScreen(
                                                    emailAddress = emailForOtp,
                                                    onBackClick = { currentScreen = Screen.SignUp },
                                                    onVerifyOtp = { otp ->
                                                        currentScreen = Screen.Login
                                                    }
                                                )
                                                Screen.Admin -> AdminScreen(
                                                    onLogoutClick = {
                                                        currentScreen = Screen.Login
                                                    }
                                                )
                                                Screen.Main -> MainScreen(
                                                    onNotificationClick = { /* TODO */ },
                                                    onMenuClick = { /* TODO */ },
                                                    onNavigate = { destination ->
                                                        when {
                                                            destination == "differ" -> currentScreen = Screen.Differ
                                                            destination == "order" -> currentScreen = Screen.Booked
                                                            destination == "orders" -> currentScreen = Screen.Cart
                                                            destination == "rewards" -> currentScreen = Screen.Coupon
                                                            destination.startsWith("product/") -> {
                                                                productId = destination.removePrefix("product/")
                                                                currentScreen = Screen.ProductDetail
                                                            }
                                                            else -> { /* Handle other navigation */ }
                                                        }
                                                    },
                                                    onNavigateToNoti = {
                                                        previousScreen = Screen.Main
                                                        currentScreen = Screen.Noti
                                                    },
                                                )
                                                Screen.ProductDetail -> {
                                                    val productDetails = when (productId) {
                                                        "xoai_granola" -> Triple(
                                                            "Smoothie Xoài Nhiệt Đới Granola",
                                                            "65.000đ",
                                                            R.drawable.xoai_granola
                                                        )
                                                        "phuc_bon_tu_granola" -> Triple(
                                                            "Smoothie Phúc Bồn Tử Granola",
                                                            "65.000đ",
                                                            R.drawable.phuc_bon_tu_granola
                                                        )
                                                        "oolong_tu_quy_vai" -> Triple(
                                                            "Oolong Tứ Quý Vải",
                                                            "59.000đ",
                                                            R.drawable.oolong_tu_quy_vai
                                                        )
                                                        "oolong_kim_quat_tran_chau" -> Triple(
                                                            "Oolong Tứ Quý Kim Quất Trân Châu",
                                                            "59.000đ",
                                                            R.drawable.oolong_kim_quat_tran_chau
                                                        )
                                                        "tra_sua_oolong_tu_quy_suong_sao" -> Triple(
                                                            "Trà Sữa Oolong Tứ Quý Sương Sáo",
                                                            "55.000đ",
                                                            R.drawable.tra_sua_oolong_tu_quy_suong_sao
                                                        )
                                                        else -> Triple("Sản phẩm", "0đ", R.drawable.xoai_granola)
                                                    }
                                                    PrdScreen(
                                                        productId = productId,
                                                        onBackClick = { currentScreen = Screen.Main },
                                                        onViewCart = { currentScreen = Screen.Cart },
                                                        onNavigateToMain = {
                                                            currentScreen = Screen.Main
                                                        }
                                                    )
                                                }
                                                Screen.Differ -> DifferScreen(
                                                    onBackClick = { currentScreen = Screen.Main },
                                                    onNavigationItemClick = { destination ->
                                                        when (destination) {
                                                            "home" -> currentScreen = Screen.Main
                                                            "order" -> currentScreen = Screen.Booked
                                                            "rewards" -> currentScreen = Screen.Coupon
                                                            "user_info" -> currentScreen = Screen.UserInfo
                                                            else -> currentScreen = Screen.Differ
                                                        }
                                                    },
                                                    onLogoutClick = {
                                                        authManager.clearLoginCredentials()
                                                        currentScreen = Screen.Login
                                                    },
                                                    onHistoryClick = {
                                                        currentScreen = Screen.History
                                                    },
                                                    onNavigateToNoti = {
                                                        previousScreen = Screen.Differ
                                                        currentScreen = Screen.Noti
                                                    }
                                                )
                                                Screen.UserInfo -> UserInfScreen(
                                                    onBackClick = { currentScreen = Screen.Differ }
                                                )
                                                Screen.Cart -> CartScreen(
                                                    onBackClick = { currentScreen = Screen.Main },
                                                    onNavigateToPayment = { order ->
                                                        selectedOrder = order
                                                        currentScreen = Screen.Payment
                                                    }
                                                )
                                                Screen.Payment -> {
                                                    selectedOrder?.let { order ->
                                                        PaymentScreen(
                                                            order = order,
                                                            onBackClick = { currentScreen = Screen.Cart },
                                                            onNavigateToMain = {
                                                                currentScreen = Screen.Main
                                                            },
                                                            onSelectVoucher = {
                                                                currentScreen = Screen.SelectVoucher
                                                            }
                                                        )
                                                    } ?: run {
                                                        currentScreen = Screen.Cart
                                                    }
                                                }
                                                Screen.SelectVoucher -> SelectVoucherScreen(
                                                    onBackClick = { currentScreen = Screen.Payment },
                                                    onVoucherSelect = { voucher ->
                                                        currentScreen = Screen.Payment
                                                    }
                                                )
                                                Screen.Coupon -> CouponScreen(
                                                    onBackClick = { currentScreen = Screen.Main },
                                                    onNavigationItemClick = { destination ->
                                                        when (destination) {
                                                            "home" -> currentScreen = Screen.Main
                                                            "order" -> currentScreen = Screen.Booked
                                                            "rewards" -> currentScreen = Screen.Coupon
                                                            "differ" -> currentScreen = Screen.Differ
                                                            else -> { /* Handle other navigation */ }
                                                        }
                                                    }
                                                )
                                                Screen.Booked -> BookedScreen(
                                                    onBackClick = { currentScreen = Screen.Main },
                                                    onNavigationItemClick = { destination ->
                                                        when (destination) {
                                                            "home" -> currentScreen = Screen.Main
                                                            "order" -> currentScreen = Screen.Booked
                                                            "rewards" -> currentScreen = Screen.Coupon
                                                            "differ" -> currentScreen = Screen.Differ
                                                            else -> { /* Handle other navigation */ }
                                                        }
                                                    },
                                                    onFavoritesClick = {
                                                        currentScreen = Screen.WishList
                                                    }
                                                )
                                                Screen.History -> HistoryScreen(
                                                    onBackClick = { currentScreen = Screen.Differ }
                                                )
                                                Screen.WishList -> WishListScreen(
                                                    onBackClick = { currentScreen = Screen.Booked }
                                                )
                                                Screen.Noti -> NotiScreen(
                                                    onBackClick = { currentScreen = previousScreen }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Enum để quản lý các màn hình
    enum class Screen {
        Login,
        ForgotPassword,
        OtpVerification,
        ChangePassword,
        SignUp,
        OtpSignUp,
        Main,
        Differ,
        ProductDetail,
        Cart,
        Payment,
        Coupon,
        Booked,
        Admin,
        UserInfo,
        History,
        WishList,
        Noti,
        SelectVoucher
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        CafeTrioTheme {
            Greeting("CafeTrio")
        }
    }
}