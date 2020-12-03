package com.victormagosso.vfood.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseConfig {
    private var firebaseRef: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseStorage: StorageReference? = null

    fun getFirebaseDatabase(): DatabaseReference {
         if(firebaseRef == null) {
             firebaseRef = FirebaseDatabase.getInstance().reference
         }
         return firebaseRef as DatabaseReference
    }
    fun getFirebaseAuth(): FirebaseAuth {
        if(firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance()
        }
        return firebaseAuth as FirebaseAuth
    }
    fun getFirebaseStorage(): StorageReference {
        if(firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance().reference
        }
        return firebaseStorage as StorageReference
    }
}