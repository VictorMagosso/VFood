package com.victormagosso.vfood.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.UserFirebaseData
import com.victormagosso.vfood.model.client.Client


class ClientService {
    var firebaseConfig = FirebaseConfig()
    var userFirbaseData = UserFirebaseData()
    private var storageReference: StorageReference? = null
    fun saveClient(client: Client) {
        val firebase: DatabaseReference = firebaseConfig.getFirebaseDatabase()
        firebase.child("clients")
            .child(client.cId!!)
            .setValue(client)
    }

//    fun saveProfileImg(imgData: ByteArray?) {
//        //esta dando errado, verificar depois
//        storageReference = firebaseConfig.getFirebaseStorage()
//        storageReference!!
//            .child("images")
//            .child("profile")
//            .child(userFirbaseData.getUid().toString())
//            .child("profileimg.jpeg")
//        val uploadTask = storageReference!!.putBytes(imgData!!)
//        uploadTask.addOnFailureListener { println("errado") }
//            .addOnSuccessListener { println("certo") }
//    }
//
//    fun getProfileImg() {
//        storageReference = firebaseConfig.getFirebaseStorage()
//        storageReference!!
//            .child("images")
//            .child("profile")
//    }
}