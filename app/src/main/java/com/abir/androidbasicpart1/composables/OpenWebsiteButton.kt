package com.abir.androidbasicpart1.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abir.androidbasicpart1.ui.theme.ThemeBgColorPrimary
import com.abir.androidbasicpart1.ui.theme.ThemeTextColor

@Composable
fun OpenWebsiteButton(websiteLink: String) {
    val context = LocalContext.current

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = ThemeTextColor),
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(websiteLink)
            }
            context.startActivity(intent)
        }
    ) {
        Text(
            text = "Go to Website",
            modifier = Modifier.padding(10.dp),
            color = ThemeBgColorPrimary,
            fontSize = 15.sp
        )
    }
}