package com.abir.androidbasicpart1.presentationlayer.composables.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableSection(
    title: String,
    isExpanded: Boolean,
    onExpandChange: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandChange() }
                .background(color = Color.LightGray)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 18.sp, modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }

        if (isExpanded) {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                content()
            }
        }
    }
}