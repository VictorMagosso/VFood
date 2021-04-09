package com.victormagosso.vfood.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.CreditCard


class CreditCardRepository {
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