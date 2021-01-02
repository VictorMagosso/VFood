package com.victormagosso.vfood.model.client

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cart_table")
data class Cart (
    @PrimaryKey(autoGenerate = true)
    var nId: Int?,
    var cProductName: String?,
    var nQuantity: String?,
    var nTotalPrice: String
)