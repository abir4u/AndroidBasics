package com.abir.androidbasicpart1.presentationlayer.screens.firebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.abir.androidbasicpart1.presentationlayer.navigation.menu.Screen
import com.abir.androidbasicpart1.datalayer.localStorage.dataStore.saveLoginState
import kotlinx.coroutines.launch

@Composable
fun LoginSuccessScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login Success!")
        Button(onClick = {
            coroutineScope.launch {
                // Set login state to false and clear username on logout
                saveLoginState(context, isLoggedIn = false, username = "")
                navController.navigate(Screen.FirebaseHome.route)
            }
        }) {
            Text("Logout")
        }
    }
}