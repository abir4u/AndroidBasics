package com.abir.androidbasicpart1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abir.androidbasicpart1.viewmodels.UserViewModel

@Composable
fun UserScreen(userViewModel: UserViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                // Fetch users when button is clicked
                userViewModel.getUsers()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Fetch Users")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if there is any
        if (userViewModel.errorMessage.isNotEmpty()) {
            Text(text = userViewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Display the list of users
        userViewModel.users.forEach { user ->
            Text(text = "ID: ${user.id}, Name: ${user.name}, Email: ${user.email}")
        }
    }
}

@Composable
fun FirebaseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Firebase")
        UserScreen()
    }
}