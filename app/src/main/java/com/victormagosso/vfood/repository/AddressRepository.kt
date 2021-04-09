package com.victormagosso.vfood.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.Address
import com.victormagosso.vfood.model.client.Client
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.model.company.Product

class AddressRepository {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()
    private var storageReference: StorageReference? = null
    fun saveSecondaryAddress(address: Address) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("addresses")
            .child(userFirbaseData.getUid()!!)
            .child("secondaryAddresses")
            .child(address.cIdAddress!!)
            .setValue(address)
    }

    fun deleteSecondaryAddress(address: Address) {
        var firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("addresses")
            .child(userFirbaseData.getUid()!!)
            .child("secondaryAddresses")
            .child(address.cIdAddress!!)
            .removeValue()
    }

    fun saveMainAddress(address: Address) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("addresses")
            .child(userFirbaseData.getUid()!!)
            .child("mainAddresses")
//            .child(address.cIdAddress!!)
            .setValue(address)
    }

    fun deleteMainAddress(address: Address) {
        var firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("addresses")
            .child(userFirbaseData.getUid()!!)
            .child("mainAddresses")
//            .child(address.cIdAddress!!)
            .removeValue()
    }
}