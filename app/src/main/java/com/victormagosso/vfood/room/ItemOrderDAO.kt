package com.victormagosso.vfood.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.victormagosso.vfood.model.order.ItemOrder

@Dao
interface ItemOrderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(itemOrder: ItemOrder)

    @Query("SELECT * FROM cart_table")
    fun readAllData(): LiveData<List<ItemOrder>>

    @Delete
    suspend fun deleteItem(itemOrder: ItemOrder)
}