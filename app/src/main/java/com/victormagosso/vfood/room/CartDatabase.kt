package com.victormagosso.vfood.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.victormagosso.vfood.model.order.ItemOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [ItemOrder::class], version = 1, exportSchema = true)
abstract class CartDatabase : RoomDatabase() {
    abstract fun itemOrderDao(): ItemOrderDAO

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, CartDatabase::class.java, "cart_database").build()
                INSTANCE = instance
                return instance
            }
        }
        fun clearAllTables() {
            GlobalScope.launch(Dispatchers.IO) {
                CartDatabase.clearAllTables()
            }
        }
    }
}