package com.abir.androidbasicpart1.presentationlayer.screens.storage.roomdb

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abir.androidbasicpart1.datalayer.models.WalletItem

@Composable
fun AddWalletItemScreen(onAdd: (WalletItem) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type") })
        OutlinedTextField(value = balance, onValueChange = { balance = it }, label = { Text("Balance") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onAdd(WalletItem(name = name, type = type, balance = balance, description = description))
        }) {
            Text("Add Item")
        }
    }
}