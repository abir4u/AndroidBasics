package com.abir.androidbasicpart1.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abir.androidbasicpart1.R
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel: ViewModel() {
    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _phoneError = MutableLiveData<String?>()
    val phoneError: LiveData<String?> = _phoneError

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to track login status
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus


    // Method to validate email and update the LiveData for errors
    fun validateEmail(email: String) {
        _emailError.value = when {
            email.isBlank() -> "Email cannot be empty"
            !Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$").matches(email) -> "Invalid email format"
            else -> null
        }
    }

    // Method to validate password and update the LiveData for errors
    fun validatePassword(password: String) {
        _passwordError.value = when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters"
            !Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$").matches(password) ->
                "Password must contain uppercase, lowercase, digit, and special character"
            else -> null
        }
    }

    // Method to validate phone number and update the LiveData for errors
    fun validatePhoneNumber(phone: String) {
        _phoneError.value = when {
            phone.isBlank() -> "Phone number cannot be empty"
            !Regex("^[+]?\\d{10,15}$").matches(phone) -> "Invalid phone number format"
            else -> null
        }
    }

    // Function to handle login
    fun signInWithEmail(context: Context, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = context.getString(R.string.login_success)
                } else {
                    _loginStatus.value = "${context.getString(R.string.login_failed)} ${task.exception?.message}"
                }
            }
    }

    fun registerWithEmail(context: Context, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = context.getString(R.string.registration_success)
                } else {
                    _loginStatus.value = "${context.getString(R.string.registration_failed)} ${task.exception?.message}"
                }
            }
    }

    // Reset status after showing to avoid displaying the same message repeatedly
    fun resetLoginStatus() {
        _loginStatus.value = ""
    }
}