package com.abir.androidbasicpart1.datalayer.localStorage.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abir.androidbasicpart1.datalayer.models.WalletItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_items")
    fun getAllItems(): Flow<List<WalletItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(walletItem: WalletItem)

    @Delete
    suspend fun deleteItem(walletItem: WalletItem)
}
