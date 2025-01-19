package com.abir.androidbasicpart1.businesslayer.viewmodels.authentication

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abir.androidbasicpart1.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to track login status
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    // LiveData to track email address
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    // LiveData to track error
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Get GoogleSignInClient
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    // Authenticate with Firebase using Google account
    fun signInWithGoogle(account: GoogleSignInAccount, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Google Sign-In failed.")
                }
            }
    }

    fun performAuthentication(context: Context, data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                signInWithGoogle(account,
                    onSuccess = {
                        _loginStatus.value = context.getString(R.string.login_success)
                        _email.value = account.email
                        _error.value = ""
                    },
                    onFailure = { errorMessage ->
                        _loginStatus.value = context.getString(R.string.login_failed)
                        _error.value = errorMessage
                    }
                )
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
