package com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.phone

import android.app.Activity
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
import com.abir.androidbasicpart1.presentationlayer.composables.common.BasicTextField
import com.abir.androidbasicpart1.presentationlayer.navigation.menu.Screen
import com.abir.androidbasicpart1.datalayer.localStorage.dataStore.saveLoginState
import com.abir.androidbasicpart1.businesslayer.viewmodels.authentication.PhoneAuthViewModel

@Composable
fun FirebasePhoneLoginScreen(navController: NavHostController, viewModel: PhoneAuthViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    val phoneError by viewModel.phoneError.observeAsState()
    val statusMessage by viewModel.statusMessage.observeAsState("Enter your phone number")
    val isCodeSent by viewModel.isCodeSent.observeAsState(false)
    val loginStatus by viewModel.loginStatus.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = statusMessage, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (!isCodeSent) {
            BasicTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    viewModel.validatePhoneNumber(it)
                },
                labelText = "Phone Number",
                errorText = phoneError,
                placeholderText = "+1234567890",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (phoneNumber.isNotBlank()) {
                    viewModel.startPhoneNumberVerification(activity, phoneNumber)
                } else {
                    viewModel.updateStatusMessage("Please enter a valid phone number.")
                }
            }) {
                Text("Send OTP")
            }
        } else {
            BasicTextField(
                value = otpCode,
                onValueChange = { otpCode = it },
                labelText = "Enter OTP",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                viewModel.validatePhoneSignInCriteria(context, otpCode)
            }) {
                Text("Verify OTP")
            }
        }

        // Observe login status and navigate to success screen if successful
        loginStatus?.let { status ->
            if (status == stringResource(R.string.login_success)) {
                LaunchedEffect(key1 = status) {
                    saveLoginState(context = context, true, phoneNumber)
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
