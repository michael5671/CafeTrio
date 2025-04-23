package com.example.cafetrio.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafetrio.R
import com.example.cafetrio.ui.theme.CafeBeige
import com.example.cafetrio.ui.theme.CafeBrown
import com.example.cafetrio.ui.theme.CafeTrioTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(onSplashFinished: () -> Unit = {}) {
    // Đếm thời gian hiển thị splash screen
    LaunchedEffect(key1 = true) {
        delay(1800) // Giảm xuống 1.8 giây
        onSplashFinished() // Chuyển qua màn hình đăng nhập
    }
    
    // Animation cho title và tagline
    val titleAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "title_alpha"
    )
    
    val taglineAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 200, // Xuất hiện sau title
            easing = FastOutSlowInEasing
        ),
        label = "tagline_alpha"
    )
    
    // Animation scale cho logo/title
    val logoScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 800,
            easing = EaseOutBack
        ),
        label = "logo_scale"
    )
    
    // Hiệu ứng fading out khi kết thúc
    val contentAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 600,
            easing = LinearEasing
        ),
        label = "content_alpha"
    )
    
    // Giao diện splash screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CafeBeige)
            .alpha(contentAlpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(logoScale)
        ) {
            // Logo text với hiệu ứng mờ dần
            Text(
                text = "Café Trio",
                color = CafeBrown,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.agbalumo_regular)),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tagline với hiệu ứng mờ dần xuất hiện sau
            Text(
                text = "\"A Trio of Taste, A Symphony of Aroma\"",
                color = CafeBrown,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    CafeTrioTheme {
        SplashScreen()
    }
} 