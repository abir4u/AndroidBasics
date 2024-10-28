package com.abir.androidbasicpart1.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abir.androidbasicpart1.R
import com.abir.androidbasicpart1.ui.theme.SplashScreenBackground
import com.abir.androidbasicpart1.ui.theme.SplashScreenText

@Composable
fun SplashScreen() {
    // Background color for the splash screen
    val backgroundColor = SplashScreenBackground

    // Content of the Splash Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.android_basics_full_screen_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(240.dp)
                    .padding(bottom = 16.dp)
            )

            // App Name
            Text(
                text = "Android Basics",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = SplashScreenText
                )
            )
        }

        // Developer Name
        Text(
            text = "Developed by Abir Pal",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp), // Padding from the bottom
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.White // Change color to match your design
            )
        )
    }
}
