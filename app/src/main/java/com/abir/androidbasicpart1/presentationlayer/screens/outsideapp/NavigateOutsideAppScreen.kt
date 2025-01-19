package com.abir.androidbasicpart1.presentationlayer.screens.outsideapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abir.androidbasicpart1.presentationlayer.composables.intent.DialButton
import com.abir.androidbasicpart1.presentationlayer.composables.intent.EmailButton
import com.abir.androidbasicpart1.presentationlayer.composables.intent.ExternalMapButton
import com.abir.androidbasicpart1.presentationlayer.composables.intent.OpenWebsiteButton

@Composable
fun NavigateOutsideAppScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Outside App")

        Spacer(modifier = Modifier.height(16.dp))

        ContactInfoRow(
            label = "Call me at:",
            icon = Icons.Default.Phone,
            content = { DialButton(phoneNumber = "02102963036") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContactInfoRow(
            label = "My location:",
            icon = Icons.Default.LocationOn,
            content = { ExternalMapButton(latitude = 37.7749, longitude = -122.4194) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContactInfoRow(
            label = "My Email:",
            icon = Icons.Default.Email,
            content = { EmailButton() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContactInfoRow(
            label = "Our Website:",
            icon = Icons.Default.ExitToApp,
            content = { OpenWebsiteButton("https://softwareimprove.wordpress.com/") }
        )
    }
}

@Composable
fun ContactInfoRow(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(120.dp)
        )
        content()
    }
}
