package com.abir.androidbasicpart1.businesslayer.viewmodels.storage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.abir.androidbasicpart1.businesslayer.repositories.WalletRepository
import com.abir.androidbasicpart1.datalayer.localStorage.roomdb.WalletDatabase
import com.abir.androidbasicpart1.datalayer.models.WalletItem
import kotlinx.coroutines.launch

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WalletRepository
    val allItems: LiveData<List<WalletItem>>

    init {
        val dao = WalletDatabase.getDatabase(application).walletDao()
        repository = WalletRepository(dao)
        allItems = repository.allItems.asLiveData()
    }

    fun addItem(item: WalletItem) = viewModelScope.launch { repository.insertItem(item) }
    fun deleteItem(item: WalletItem) = viewModelScope.launch { repository.deleteItem(item) }
}