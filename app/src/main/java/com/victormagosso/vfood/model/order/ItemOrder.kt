package com.victormagosso.vfood.model.order

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cart_table")
data class ItemOrder (
    @PrimaryKey(autoGenerate = true)
    var nIdItemOrder: Int,
    var cIdProduct: String?,
    var cProductName: String?,
    var nQuantity: Int?,
    var nTotalPrice: Double?,
    var cObservation: String?
)