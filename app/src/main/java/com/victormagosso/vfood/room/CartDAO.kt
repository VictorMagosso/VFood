package com.victormagosso.vfood.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.victormagosso.vfood.model.client.Cart

@Dao
interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(itemCart: Cart)

    @Query("SELECT * FROM cart_table ORDER BY nId")
    fun readAllData(): LiveData<List<Cart>>
}