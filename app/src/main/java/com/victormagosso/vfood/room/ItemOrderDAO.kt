package com.victormagosso.vfood.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.victormagosso.vfood.model.order.ItemOrder

@Dao
interface ItemOrderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(itemOrder: ItemOrder)

    @Query("SELECT * FROM cart_table")
    fun readAllData(): LiveData<List<ItemOrder>>

//    @Query("DELETE FROM cart_table WHERE nIdItemOrder = 0")
}