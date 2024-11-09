package com.abir.androidbasicpart1.screens.firebase.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.abir.androidbasicpart1.localstorage.dataStore.getLoginState
import com.abir.androidbasicpart1.screens.firebase.LoginSuccessScreen
import kotlinx.coroutines.launch

@Composable
fun FirebaseHomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Collect login state from DataStore
    var isLoggedIn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Collect the login state from DataStore
    LaunchedEffect(Unit) {
        // Wait for the login state to be fetched
        coroutineScope.launch {
            getLoginState(context).collect { loginState ->
                isLoggedIn = loginState
                isLoading = false
            }
        }
    }

    // Show loading screen until data is fetched
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (isLoggedIn) {
            LoginSuccessScreen(navController = navController)
        } else {
            FirebaseLoginHomeScreen(navController = navController)
        }
    }
}