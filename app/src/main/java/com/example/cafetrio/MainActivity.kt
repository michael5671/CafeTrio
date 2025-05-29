package com.example.cafetrio

import android.os.Bundle
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
import com.example.cafetrio.data.models.Order
import com.example.cafetrio.ui.AdminScreen
import com.example.cafetrio.ui.BookedScreen
import com.example.cafetrio.ui.CartScreen
import com.example.cafetrio.ui.ChangePasswordScreen
import com.example.cafetrio.ui.CouponScreen
import com.example.cafetrio.ui.DifferScreen
import com.example.cafetrio.ui.ForgotPasswordScreen
import com.example.cafetrio.ui.LoginScreen
import com.example.cafetrio.ui.MainScreen
import com.example.cafetrio.ui.OTP_FGPassScreen
import com.example.cafetrio.ui.OTP_SignUpScreen
import com.example.cafetrio.ui.PaymentScreen
import com.example.cafetrio.ui.PrdScreen
import com.example.cafetrio.ui.SignUpScreen
import com.example.cafetrio.ui.SplashScreen
import com.example.cafetrio.ui.theme.CafeTrioTheme
import com.example.cafetrio.ui.UserInfScreen
import com.example.cafetrio.ui.HistoryScreen
import com.example.cafetrio.ui.WishListScreen
import com.example.cafetrio.ui.NotiScreen
import com.example.cafetrio.ui.SelectVoucherScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeTrioTheme {
                var showSplash by remember { mutableStateOf(true) }
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                var previousScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                var emailForOtp by remember { mutableStateOf("") }
                var otpToken by remember { mutableStateOf("") }
                var productId by remember { mutableStateOf("") }
                var selectedOrder: Order? by remember { mutableStateOf(null) }
                
                // Get AuthManager instance
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
                        // Chuyển đổi giữa các màn hình với hiệu ứng
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                when {
                                    // Use a nicer fade + slide transition for Login to Main transition
                                    targetState == Screen.Main && initialState == Screen.Login -> {
                                        (slideInHorizontally(animationSpec = tween(900)) { width -> width } + 
                                        fadeIn(animationSpec = tween(900))) togetherWith
                                        (slideOutHorizontally(animationSpec = tween(900)) { width -> -width } + 
                                        fadeOut(animationSpec = tween(900)))
                                    }
                                    // Use the same transition for Splash to Login for consistency
                                    initialState == Screen.Login && targetState == Screen.Main -> {
                                        fadeIn(animationSpec = tween(800)) togetherWith
                                        fadeOut(animationSpec = tween(800))
                                    }
                                    // Faster transition for ProductDetail to Main
                                    targetState == Screen.Main && initialState == Screen.ProductDetail -> {
                                        fadeIn(animationSpec = tween(150)) togetherWith
                                        fadeOut(animationSpec = tween(150))
                                    }
                                    // Default transition for other screens
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
                                        // Kiểm tra email admin
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
                                        // Chuyển đến màn hình đổi mật khẩu sau khi xác thực OTP
                                        otpToken = otp
                                        currentScreen = Screen.ChangePassword
                                    }
                                )
                                Screen.ChangePassword -> ChangePasswordScreen(
                                    email = emailForOtp,
                                    onBackClick = { currentScreen = Screen.OtpVerification },
                                    onChangePasswordSubmit = {
                                        // Quay về màn hình đăng nhập sau khi đổi mật khẩu
                                        currentScreen = Screen.Login
                                    }
                                )
                                Screen.SignUp -> SignUpScreen(
                                    onBackClick = { currentScreen = Screen.Login },
                                    onSignUpSubmit = { email ->
                                        // Chuyển đến màn hình xác thực OTP khi đăng ký
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
                                        // Xác thực OTP thành công, chuyển về màn hình đăng nhập
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
                                    }
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
                                            // Navigate back to MainScreen when success dialog is dismissed
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
                                    onOrderClick = { order -> 
                                        selectedOrder = order
                                        currentScreen = Screen.Payment
                                    }
                                )
                                Screen.Payment -> {
                                    selectedOrder?.let { order ->
                                        PaymentScreen(
                                            order = order,
                                            onBackClick = { currentScreen = Screen.Cart },
                                            onPlaceOrderClick = {
                                                // This is no longer needed since we're handling navigation through onNavigateToMain
                                            },
                                            onNavigateToMain = {
                                                // Navigate to main screen when the success dialog is dismissed
                                                currentScreen = Screen.Main
                                            },
                                            onSelectVoucher = {
                                                currentScreen = Screen.SelectVoucher
                                            }
                                        )
                                    } ?: run {
                                        // Fallback if no order is selected
                                        currentScreen = Screen.Cart
                                    }
                                }
                                Screen.SelectVoucher -> SelectVoucherScreen(
                                    onBackClick = { currentScreen = Screen.Payment },
                                    onVoucherSelect = { voucher ->
                                        // Handle voucher selection
                                        currentScreen = Screen.Payment
                                    }
                                )
                                Screen.Coupon -> {
                                    CouponScreen(
                                        onBackClick = { currentScreen = Screen.Main },
                                        onNavigationItemClick = { destination ->
                                            when (destination) {
                                                "home" -> currentScreen = Screen.Main
                                                "order" -> currentScreen = Screen.Booked
                                                "rewards" -> currentScreen = Screen.Coupon // Navigate to itself (refresh)
                                                "differ" -> currentScreen = Screen.Differ
                                                else -> { /* Handle other navigation */ }
                                            }
                                        }
                                    )
                                }
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