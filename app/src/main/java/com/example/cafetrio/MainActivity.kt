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
import com.example.cafetrio.ui.ChangePasswordScreen
import com.example.cafetrio.ui.ForgotPasswordScreen
import com.example.cafetrio.ui.LoginScreen
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
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
                var phoneForOtp by remember { mutableStateOf("") }
                
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
                                fadeIn(animationSpec = tween(500)) togetherWith
                                fadeOut(animationSpec = tween(500))
                            },
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                Screen.Login -> LoginScreen(
                                    onForgotPasswordClick = { currentScreen = Screen.ForgotPassword },
                                    onSignUpClick = { currentScreen = Screen.SignUp }
                                )
                                Screen.ForgotPassword -> ForgotPasswordScreen(
                                    onBackToLogin = { currentScreen = Screen.Login },
                                    onSubmitPhone = { phone ->
                                        phoneForOtp = phone
                                        currentScreen = Screen.OtpVerification
                                    }
                                )
                                Screen.OtpVerification -> OTP_FGPassScreen(
                                    phoneNumber = phoneForOtp,
                                    onBackClick = { currentScreen = Screen.ForgotPassword },
                                    onVerifyOtp = { otp ->
                                        // Chuyển đến màn hình đổi mật khẩu sau khi xác thực OTP
                                        currentScreen = Screen.ChangePassword
                                    }
                                )
                                Screen.ChangePassword -> ChangePasswordScreen(
                                    onBackClick = { currentScreen = Screen.OtpVerification },
                                    onChangePasswordSubmit = {
                                        // Quay về màn hình đăng nhập sau khi đổi mật khẩu
                                        currentScreen = Screen.Login
                                    }
                                )
                                Screen.SignUp -> SignUpScreen(
                                    onBackClick = { currentScreen = Screen.Login },
                                    onSignUpSubmit = { phone ->
                                        // Chuyển đến màn hình xác thực OTP khi đăng ký
                                        phoneForOtp = phone
                                        currentScreen = Screen.OtpSignUp
                                    }
                                )
                                Screen.OtpSignUp -> OTP_SignUpScreen(
                                    phoneNumber = phoneForOtp,
                                    onBackClick = { currentScreen = Screen.SignUp },
                                    onVerifyOtp = { otp ->
                                        // Xác thực OTP thành công, chuyển về màn hình đăng nhập
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
    OtpSignUp
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