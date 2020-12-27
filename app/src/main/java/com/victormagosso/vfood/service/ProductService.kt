package com.victormagosso.vfood.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.company.Product


class ProductService {
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