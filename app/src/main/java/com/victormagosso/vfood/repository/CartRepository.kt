package com.victormagosso.vfood.repository

import androidx.lifecycle.LiveData
import com.victormagosso.vfood.model.order.ItemOrder
import com.victormagosso.vfood.room.ItemOrderDAO

class CartRepository(private val itemOrderDao: ItemOrderDAO) {
    val readAllData: LiveData<List<ItemOrder>> = itemOrderDao.readAllData()

    suspend fun addItem(itemOrder: ItemOrder) {
        itemOrderDao.addItem(itemOrder)
    }
}