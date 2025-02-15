package com.abir.androidbasicpart1.businesslayer.viewmodels.authentication

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abir.androidbasicpart1.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class PhoneAuthViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _phoneError = MutableLiveData<String?>()
    val phoneError: LiveData<String?> = _phoneError

    private val _isCodeSent = MutableLiveData(false)
    val isCodeSent: LiveData<Boolean> get() = _isCodeSent

    private val _verificationId = MutableLiveData<String?>()
    private var activityReference: WeakReference<Activity>? = null

    // LiveData to track login status
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    private val _statusMessage = MutableLiveData("Enter your phone number")
    val statusMessage: LiveData<String> get() = _statusMessage

    // Update status message
    fun updateStatusMessage(message: String) {
        _statusMessage.value = message
    }

    // Method to validate phone number and update the LiveData for errors
    fun validatePhoneNumber(phone: String) {
        _phoneError.value = when {
            phone.isBlank() -> "Phone number cannot be empty"
            !Regex("^[+]?\\d{10,15}$").matches(phone) -> "Invalid phone number format"
            else -> null
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            updateStatusMessage("Verification completed successfully!")
            signInWithPhoneAuthCredential(activityReference?.get(), credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            updateStatusMessage("Verification failed: ${e.message}")

            when (e) {
                is FirebaseAuthInvalidCredentialsException -> updateStatusMessage("The request is invalid.")
                is FirebaseTooManyRequestsException -> updateStatusMessage("The SMS quota for the project has been exceeded.")
                is FirebaseAuthMissingActivityForRecaptchaException -> updateStatusMessage("reCAPTCHA verification attempted with null Activity.")
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            updateStatusMessage("Code sent. Please enter the OTP.")
            _isCodeSent.value = true
            _verificationId.value = verificationId
        }
    }


    // Function to sign in with phone auth credential
    private fun signInWithPhoneAuthCredential(context: Context?, credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = context?.getString(R.string.login_success)
                    // Trigger navigation or status update here if needed
                } else {
                    _loginStatus.value = "${context?.getString(R.string.login_failed)} ${task.exception?.message}"
                }
            }
    }

    // Start the phone number verification process
    fun startPhoneNumberVerification(activity: Activity, phoneNumber: String) {
        activityReference = WeakReference(activity)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Verify OTP entered by the user
    fun validatePhoneSignInCriteria(context: Context, otpCode: String) {
        val verificationId = _verificationId.value
        if (!verificationId.isNullOrEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
            signInWithPhoneAuthCredential(context, credential)
        } else {
            updateStatusMessage("Verification ID is missing.")
        }
    }

    // Reset status after showing to avoid displaying the same message repeatedly
    fun resetLoginStatus() {
        _loginStatus.value = ""
    }
}