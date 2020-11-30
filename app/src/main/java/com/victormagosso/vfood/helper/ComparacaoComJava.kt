//package com.victormagosso.vfood.helper
//
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.storage.StorageReference
//
//object ComparacaoComJava {
//    var firebaseRef: DatabaseReference? = null
//    var firebaseAuth: FirebaseAuth? = null
//    var firebaseStorage: StorageReference? = null
//    fun getFirebaseRef(): DatabaseReference? {
//        if (firebaseRef == null) {
//            firebaseRef = FirebaseDatabase.getInstance().reference
//        }
//        return firebaseRef
//    }
//}