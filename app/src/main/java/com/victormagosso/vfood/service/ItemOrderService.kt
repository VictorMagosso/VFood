package com.victormagosso.vfood.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.order.ItemOrder
import com.victormagosso.vfood.model.order.Order


class ItemOrderService {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()
    private var storageReference: StorageReference? = null
    fun saveOrder(itemOrder: ItemOrder) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("orders")
            .child(userFirbaseData.getUid()!!)
//            .child(itemOrder.cIdOrder!!)
//            .setValue(order)
    }
    fun deleteOrder(order: Order) {
        var firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("orders")
            .child(userFirbaseData.getUid()!!)
            .child(order.cIdOrder!!)
            .removeValue()
    }
}