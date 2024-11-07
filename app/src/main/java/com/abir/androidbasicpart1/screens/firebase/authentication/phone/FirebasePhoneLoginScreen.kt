package com.abir.androidbasicpart1.screens.firebase.authentication.phone

import android.app.Activity
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abir.androidbasicpart1.composables.navigation.Screen
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun FirebasePhoneLoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as Activity

    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var otpCode by remember { mutableStateOf(TextFieldValue("")) }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var statusMessage by remember { mutableStateOf("Enter your phone number") }
    var isCodeSent by remember { mutableStateOf(false) }

    // Firebase Auth instance
    val auth = FirebaseAuth.getInstance()

    // Callbacks for phone authentication
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Automatically verifies if the OTP is correct
            statusMessage = "Verification completed successfully!"
            Log.d("PhoneAuth", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(auth, credential, navController)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            statusMessage = "Verification failed: ${e.message}"
            Log.w("PhoneAuth", "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                statusMessage = "The request is invalid"
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                statusMessage = "The SMS quota for the project has been exceeded"
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                statusMessage = "reCAPTCHA verification attempted with null Activity"
            }

        }

        override fun onCodeSent(receivedVerificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            statusMessage = "Code sent. Please enter the OTP."
            isCodeSent = true
            verificationId = receivedVerificationId
            Log.d("PhoneAuth", "onCodeSent:$verificationId")
        }
    }

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
            // Phone Number Input
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                placeholder = { Text("+1234567890") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (phoneNumber.text.isNotBlank()) {
                    startPhoneNumberVerification(activity, phoneNumber.text, callbacks)
                } else {
                    statusMessage = "Please enter a valid phone number."
                }
            }) {
                Text("Send OTP")
            }
        } else {
            // OTP Code Input
            TextField(
                value = otpCode,
                onValueChange = { otpCode = it },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (verificationId != null && otpCode.text.isNotBlank()) {
                    val credential = PhoneAuthProvider.getCredential(verificationId!!, otpCode.text)
                    signInWithPhoneAuthCredential(auth, credential, navController)
                } else {
                    statusMessage = "Please enter the OTP."
                }
            }) {
                Text("Verify OTP")
            }
        }
    }
}

// Function to start the phone number verification process
fun startPhoneNumberVerification(activity: Activity, phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
    val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
        .setPhoneNumber(phoneNumber) // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(activity) // Activity for callback binding
        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

// Function to sign in with phone auth credential
fun signInWithPhoneAuthCredential(auth: FirebaseAuth, credential: PhoneAuthCredential, navHostController: NavHostController) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("PhoneAuth", "Sign-in success")
                navHostController.navigate(Screen.LoginSuccess.route)
            } else {
                Log.e("PhoneAuth", "Sign-in failed", task.exception)
            }
        }
}