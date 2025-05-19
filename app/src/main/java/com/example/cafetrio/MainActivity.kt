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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import android.content.Context
import com.example.cafetrio.ui.ChangePasswordScreen
import com.example.cafetrio.ui.DifferScreen
import com.example.cafetrio.ui.ForgotPasswordScreen
import com.example.cafetrio.ui.LoginScreen
import com.example.cafetrio.ui.MainScreen
import com.example.cafetrio.ui.OTP_FGPassScreen
import com.example.cafetrio.ui.OTP_SignUpScreen
import com.example.cafetrio.ui.SignUpScreen
import com.example.cafetrio.ui.SplashScreen
import com.example.cafetrio.ui.theme.CafeTrioTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeTrioTheme {
                var showSplash by remember { mutableStateOf(true) }
                val context = LocalContext.current
                val sharedPreferences = remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
                var currentScreen by remember { 
                    mutableStateOf<Screen>(if (sharedPreferences.getString("auth_token", null) != null) Screen.Main else Screen.Login) 
                }
                var emailForOtp by remember { mutableStateOf("") }
                var otpToken by remember { mutableStateOf("") }
                
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
                                    onLoginClick = { currentScreen = Screen.Main }
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
                                Screen.Main -> MainScreen(
                                    onNotificationClick = { /* TODO */ },
                                    onMenuClick = { /* TODO */ },
                                    onNavigate = { destination ->
                                        when (destination) {
                                            "differ" -> currentScreen = Screen.Differ
                                            else -> { /* Handle other navigation */ }
                                        }
                                    }
                                )
                                Screen.Differ -> DifferScreen(
                                    onNavigationItemClick = { destination ->
                                        when (destination) {
                                            "home" -> currentScreen = Screen.Main
                                            else -> { /* Handle other navigation */ }
                                        }
                                    },
                                    onLogoutClick = {
                                        // Navigate back to Login screen when logging out
                                        currentScreen = Screen.Login
                                    }
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
    Differ
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