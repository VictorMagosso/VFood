package com.victormagosso.vfood.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.victormagosso.vfood.model.client.Cart

@Database(version = 1, entities = [Cart::class])
abstract class RoomDb : RoomDatabase() {


}