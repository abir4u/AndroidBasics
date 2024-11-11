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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.abir.androidbasicpart1.R
import com.abir.androidbasicpart1.composables.common.BasicTextField
import com.abir.androidbasicpart1.composables.navigation.Screen
import com.abir.androidbasicpart1.localstorage.dataStore.saveLoginState
import com.abir.androidbasicpart1.viewmodels.authentication.AuthenticationViewModel

@Composable
fun FirebaseEmailRegisterScreen(navController: NavHostController, viewModel: AuthenticationViewModel = viewModel()) {
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
        BasicTextField(
            value = email,
            onValueChange =
            {
                email = it
                viewModel.validateEmail(it)
            },
            labelText = "Email",
            errorText = emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = password,
            onValueChange =
            {
                password = it
                viewModel.validatePassword(it)
            },
            labelText = "Password",
            errorText = passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { viewModel.registerWithEmail(context, email, password) }) {
            Text("Register")

        }
        // Observe login status and navigate to success screen if successful
        loginStatus?.let { status ->
            if (status == stringResource(R.string.registration_success)) {
                LaunchedEffect(key1 = status) {
                    saveLoginState(context = context, true, email)
                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.LoginSuccess.route)
                    viewModel.resetLoginStatus() // Clear status to prevent repeated navigation
                }
            } else if (status.isNotEmpty()) {
                LaunchedEffect(key1 = status) {
                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    viewModel.resetLoginStatus() // Clear status after displaying
                }
            }
        }
    }
}