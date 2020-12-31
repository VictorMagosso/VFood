package com.victormagosso.vfood.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.CreditCard
import com.victormagosso.vfood.model.company.Product


class CreditCardService {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()
    private var storageReference: StorageReference? = null
    fun saveCard(creditCard: CreditCard) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("cards")
            .child(userFirbaseData.getUid()!!)
            .child(creditCard.cIdCard!!)
            .setValue(creditCard)
    }

    fun deleteCard (creditCard: CreditCard) {
        var firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase
            .child("cards")
            .child(userFirbaseData.getUid()!!)
            .child(creditCard.cIdCard!!)
            .removeValue()
    }
}