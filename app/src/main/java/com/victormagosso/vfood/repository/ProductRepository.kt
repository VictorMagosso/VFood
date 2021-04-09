package com.victormagosso.vfood.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product

class ProductRepository {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()
    private var storageReference: StorageReference? = null
    fun saveProduct(product: Product) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("products")
            .child(userFirbaseData.getUid()!!)
            .child(product.cIdProduct!!)
            .setValue(product)
    }
    fun deleteProduct(product: Product) {
        var firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("products")
            .child(userFirbaseData.getUid()!!)
            .child(product.cIdProduct!!)
            .removeValue()
    }
}