package com.abir.androidbasicpart1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.abir.androidbasicpart1.presentationlayer.screens.SplashScreen
import com.abir.androidbasicpart1.presentationlayer.screens.AppLandingScreen
import com.abir.androidbasicpart1.ui.theme.AndroidBasicPart1Theme
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) { initializationStatus ->
            // Optional: Handle initialization callbacks
        }
        setContent {
            AndroidBasicPart1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        delay(4000) // Display the splash screen for 2 seconds
                        isLoading = false // Navigate to the main content
                    }

                    // Use default splash screen if the device is running Android 12 or later
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        AppLandingScreen()
                    } else {
                        if (isLoading) {
                            SplashScreen()
                        } else {
                            AppLandingScreen()
                        }
                    }
                }
            }
        }
    }
}