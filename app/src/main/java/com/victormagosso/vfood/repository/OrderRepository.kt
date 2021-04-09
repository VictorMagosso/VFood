package com.victormagosso.vfood.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product
import com.victormagosso.vfood.model.order.Order

class OrderRepository {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()
    private var storageReference: StorageReference? = null
    fun saveOrder(order: Order) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("orders")
            .child(userFirbaseData.getUid()!!)
            .child(order.cIdOrder!!)
            .setValue(order)
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