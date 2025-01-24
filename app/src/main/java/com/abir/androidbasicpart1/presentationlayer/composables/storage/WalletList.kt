package com.abir.androidbasicpart1.presentationlayer.composables.storage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abir.androidbasicpart1.datalayer.models.WalletItem

@Composable
fun WalletList(walletItems: List<WalletItem>, onClick: (WalletItem) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        items(walletItems.size) { itemNumber ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onClick(walletItems[itemNumber]) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(walletItems[itemNumber].name, style = MaterialTheme.typography.headlineSmall)
                    Text(walletItems[itemNumber].type, style = MaterialTheme.typography.bodyMedium)
                    Text("Balance: ${walletItems[itemNumber].balance}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
