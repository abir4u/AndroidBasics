package com.abir.androidbasicpart1.datalayer.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_items")
data class WalletItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val balance: String,
    val description: String
)
