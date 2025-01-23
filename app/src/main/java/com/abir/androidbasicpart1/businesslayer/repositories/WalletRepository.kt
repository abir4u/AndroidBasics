package com.abir.androidbasicpart1.businesslayer.repositories

import com.abir.androidbasicpart1.datalayer.localStorage.roomdb.WalletDao
import com.abir.androidbasicpart1.datalayer.models.WalletItem
import kotlinx.coroutines.flow.Flow

class WalletRepository(private val dao: WalletDao) {
    val allItems: Flow<List<WalletItem>> = dao.getAllItems()

    suspend fun insertItem(walletItem: WalletItem) = dao.insertItem(walletItem)
    suspend fun deleteItem(walletItem: WalletItem) = dao.deleteItem(walletItem)
}