package com.victormagosso.vfood.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.victormagosso.vfood.model.order.ItemOrder

@Database(version = 1, entities = [ItemOrder::class])
abstract class RoomDb : RoomDatabase() {

}