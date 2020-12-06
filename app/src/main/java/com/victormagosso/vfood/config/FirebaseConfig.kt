package com.victormagosso.vfood.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.helper.Base64Custom

class FirebaseConfig {
    private var firebaseRef: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseStorage: StorageReference? = null
    private var base64Custom = Base64Custom()

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
    //só depois que o usuario ja está logado
    fun getUserRef(): DatabaseReference {
        var userEmail: String = firebaseAuth?.currentUser!!.email!!
        var uid: String = base64Custom.encodeToBase64(userEmail).toString()
        var userRef: DatabaseReference = firebaseRef?.child("users")!!.child(uid);
        return userRef
    }
}