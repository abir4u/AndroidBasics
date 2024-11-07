package com.abir.androidbasicpart1.screens.firebase.authentication.email

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.abir.androidbasicpart1.R
import com.abir.androidbasicpart1.composables.navigation.Screen
import com.abir.androidbasicpart1.viewmodels.AuthenticationViewModel

@Composable
fun FirebaseEmailLoginScreen(navController: NavHostController, viewModel: AuthenticationViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginStatus by viewModel.loginStatus.observeAsState()
    val emailError by viewModel.emailError.observeAsState()
    val passwordError by viewModel.passwordError.observeAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.validateEmail(it)
            },
            label = { Text("Email") },
            isError = emailError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange =
            {
                password = it
                viewModel.validatePassword(it)
            },
            label = { Text("Password") },
            isError = passwordError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.signInWithEmail(context, email, password) }) {
            Text("Login")
        }
        // Observe login status and navigate to success screen if successful
        loginStatus?.let { status ->
            if (status == stringResource(R.string.login_success)) {
                Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.LoginSuccess.route)
                viewModel.resetLoginStatus() // Clear status to prevent repeated navigation
            } else if (status.isNotEmpty()) {
                Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                viewModel.resetLoginStatus() // Clear status after displaying
            }
        }
    }
}
