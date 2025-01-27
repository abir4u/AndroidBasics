package com.abir.androidbasicpart1.presentationlayer.screens.notification

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.messaging.FirebaseMessaging

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen() {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

    // Track if the permission has been granted or not
    val hasPermission = permissionState.status.isGranted

    // Request permission on first load if not granted
    LaunchedEffect(Unit) {
        if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(70.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (hasPermission) {
                // Permission granted
                Button(onClick = { subscribeToTopic("news", context) }) {
                    Text("Subscribe to News")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { unsubscribeFromTopic("news", context) }) {
                    Text("Unsubscribe from News")
                }
            } else {
                // Permission not granted
                Text(
                    text = "Notification permissions are required to receive updates.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("Grant Notification Permission")
                }
            }
        } else {
            // For devices < Android 13, proceed without permissions
            Button(onClick = { subscribeToTopic("news", context) }) {
                Text("Subscribe to News")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { unsubscribeFromTopic("news", context) }) {
                Text("Unsubscribe from News")
            }
        }
    }
}

fun subscribeToTopic(topic: String, context: Context) {
    FirebaseMessaging.getInstance().subscribeToTopic(topic)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Subscribed to $topic", Toast.LENGTH_SHORT).show()
                Log.d("FCM", "Subscribed to $topic topic")
            } else {
                Toast.makeText(context, "Failed to subscribe to $topic", Toast.LENGTH_SHORT).show()
                Log.w("FCM", "Subscription failed", task.exception)
            }
        }
}

fun unsubscribeFromTopic(topic: String, context: Context) {
    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Unsubscribed from $topic", Toast.LENGTH_SHORT).show()
                Log.d("FCM", "Unsubscribed from $topic topic")
            } else {
                Toast.makeText(context, "Failed to unsubscribe from $topic", Toast.LENGTH_SHORT).show()
                Log.w("FCM", "Failed to unsubscribe", task.exception)
            }
        }
}
