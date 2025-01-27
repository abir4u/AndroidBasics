package com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.google

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.abir.androidbasicpart1.R
import com.abir.androidbasicpart1.presentationlayer.navigation.Screen
import com.abir.androidbasicpart1.datalayer.localStorage.dataStore.saveLoginState
import com.abir.androidbasicpart1.businesslayer.viewmodels.authentication.GoogleAuthViewModel

@Composable
fun FirebaseGoogleLoginScreen(navController: NavHostController, viewModel: GoogleAuthViewModel = viewModel()) {
    val context = LocalContext.current
    val statusMessage by remember { mutableStateOf("Click to sign in with Google") }
    val loginStatus by viewModel.loginStatus.observeAsState()
    val email by viewModel.email.observeAsState()
    val error by viewModel.error.observeAsState()

    // Launcher for the Google Sign-In intent
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            viewModel.performAuthentication(context, result.data)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = statusMessage, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { launcher.launch(viewModel.getGoogleSignInClient(context).signInIntent) }) {
            Text("Login with Google")
        }
    }

    if (loginStatus == stringResource(R.string.login_success)) {
        LaunchedEffect(key1 = loginStatus) {
            saveLoginState(context = context, true, email.toString())
            Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.LoginSuccess.route)
        }
    } else if (!error.isNullOrBlank()) {
        LaunchedEffect(key1 = error) {
            Toast.makeText(context, "Google Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}
