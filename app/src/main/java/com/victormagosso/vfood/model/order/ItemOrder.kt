package com.victormagosso.vfood.model.order

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

class ItemOrder() : Serializable, Parcelable {
    var cIdProduto: String? = ""

    var cProductName: String? = ""

    var nQuantity: Int? = 0

    var nTotalPrice: Double? = 0.0

    var cObservation: String? = ""

    constructor(parcel: Parcel) : this() {
        cIdProduto = parcel.readString()
        cProductName = parcel.readString()
        nQuantity = parcel.readValue(Int::class.java.classLoader) as? Int
        nTotalPrice = parcel.readValue(Double::class.java.classLoader) as? Double
        cObservation = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cIdProduto)
        parcel.writeString(cProductName)
        parcel.writeValue(nQuantity)
        parcel.writeValue(nTotalPrice)
        parcel.writeString(cObservation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemOrder> {
        override fun createFromParcel(parcel: Parcel): ItemOrder {
            return ItemOrder(parcel)
        }

        override fun newArray(size: Int): Array<ItemOrder?> {
            return arrayOfNulls(size)
        }
    }
}