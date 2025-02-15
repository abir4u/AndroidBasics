package com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abir.androidbasicpart1.presentationlayer.navigation.Screen

@Composable
fun FirebaseLoginHomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login via:")
        Button(onClick = { navController.navigate(Screen.EmailLogin.route) }) {
            Text("Email address")
        }
        Button(onClick = { navController.navigate(Screen.PhoneLogin.route) }) {
            Text("Phone number")
        }
        Button(onClick = { navController.navigate(Screen.GoogleLogin.route) }) {
            Text("Google")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Don't have a login?")
        Button(onClick = { navController.navigate(Screen.RegisterHome.route) }) {
            Text("Register")
        }
    }
}