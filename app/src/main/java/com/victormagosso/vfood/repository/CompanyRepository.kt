package com.victormagosso.vfood.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.company.Company

class CompanyRepository {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()
    private var storageReference: StorageReference? = null
    fun saveCompany(company: Company) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase.child("companies")
            .child(company.cId!!)
            .setValue(company)
    }
}