package com.victormagosso.vfood.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseConfig {
    private var firebaseRef: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseStorage: StorageReference? = null

    private fun getUID(): String? {
        val auth: FirebaseAuth = getFirebaseAuth()
        return auth.currentUser?.uid
    }
    private fun getFirebaseReference(): DatabaseReference {
         if(firebaseRef == null) {
             firebaseRef = FirebaseDatabase.getInstance().reference
         }
         return firebaseRef as DatabaseReference
    }
    private fun getFirebaseAuth(): FirebaseAuth {
        if(firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance()
        }
        return firebaseAuth as FirebaseAuth
    }
    private fun getFirebaseStorage(): StorageReference {
        if(firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance().reference
        }
        return firebaseStorage as StorageReference
    }
}