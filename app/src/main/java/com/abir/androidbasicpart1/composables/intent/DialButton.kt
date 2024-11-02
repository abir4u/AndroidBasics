package com.abir.androidbasicpart1.composables.intent

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
fun DialButton(phoneNumber: String) {

    // on below line we are creating
    // a variable for a context
    val ctx = LocalContext.current

    // on below line we are creating a column
    Column {
        // on below line adding a button to open URL
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = ThemeTextColor),
            onClick = {
                // on below line we are opening the dialer of our
                // phone and passing phone number.
                // Use format with "tel:" and phoneNumber created is
                // stored in u.
                val u = Uri.parse("tel: $phoneNumber")

                // Create the intent and set the data for the
                // intent as the phone number.
                val i = Intent(Intent.ACTION_DIAL, u)
                try {
                    // Launch the Phone app's dialer with a phone
                    // number to dial a call.
                    ctx.startActivity(i)
                } catch (s: SecurityException) {
                    // show() method display the toast with
                    // exception message.
                    Toast.makeText(ctx, "An error occurred", Toast.LENGTH_LONG)
                        .show()
                }
            }
        ) {
            // on below line creating a text for our button.
            Text(
                // on below line adding a text ,
                // padding, color and font size.
                text = phoneNumber,
                modifier = Modifier.padding(10.dp),
                color = ThemeBgColorPrimary,
                fontSize = 15.sp
            )
        }
    }
}