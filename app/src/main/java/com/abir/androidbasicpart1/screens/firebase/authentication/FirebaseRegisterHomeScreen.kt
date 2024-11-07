package com.abir.androidbasicpart1.screens.firebase.authentication

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
import com.abir.androidbasicpart1.composables.navigation.Screen

@Composable
fun FirebaseRegisterHomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register via:")
        Button(onClick = { navController.navigate(Screen.EmailRegister.route) }) {
            Text("Email address")
        }
        Button(onClick = { navController.navigate(Screen.PhoneRegister.route) }) {
            Text("Phone number")
        }
    }
}