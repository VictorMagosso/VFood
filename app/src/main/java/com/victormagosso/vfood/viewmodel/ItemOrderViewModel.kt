package com.victormagosso.vfood.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.victormagosso.vfood.room.CartDatabase
import com.victormagosso.vfood.repository.CartRepository
import com.victormagosso.vfood.model.order.ItemOrder
import com.victormagosso.vfood.room.RoomDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemOrderViewModel(application: Application): AndroidViewModel(application) {

    fun clearAllData() {
        CartDatabase.clearAllTables()
    }

    val readAllData: LiveData<List<ItemOrder>>
    private val repository: CartRepository

    init {
        val itemOrderDao = CartDatabase.getDatabase(application).itemOrderDao()
        repository = CartRepository(itemOrderDao)
        readAllData = repository.readAllData
    }

    fun addItem(itemOrder: ItemOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(itemOrder)
        }
    }

    fun deleteItem(itemOrder: ItemOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(itemOrder)
        }
    }
}