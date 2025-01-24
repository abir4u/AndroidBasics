package com.abir.androidbasicpart1.presentationlayer.screens.storage.roomdb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abir.androidbasicpart1.datalayer.models.WalletItem

@Composable
fun WalletDetailsScreen(item: WalletItem) {
    Column(
        modifier = Modifier.padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        Text(item.name, style = MaterialTheme.typography.headlineSmall)
        Text("Type: ${item.type}", style = MaterialTheme.typography.bodyLarge)
        Text("Balance: ${item.balance}", style = MaterialTheme.typography.bodyLarge)
        Text("Description: ${item.description}", style = MaterialTheme.typography.bodyMedium)
    }
}