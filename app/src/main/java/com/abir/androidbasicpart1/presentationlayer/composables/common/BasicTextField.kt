package com.abir.androidbasicpart1.presentationlayer.composables.common

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholderText: String = "",
    singleLine: Boolean = true,
    modifier: Modifier = Modifier.width(250.dp),
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        isError = errorText != null,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        placeholder = { Text(placeholderText) },
        singleLine = singleLine,
        modifier = modifier
    )
    if (errorText != null) {
        Text(
            text = errorText!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}