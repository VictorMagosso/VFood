package com.victormagosso.vfood.helper

import com.google.firebase.auth.FirebaseAuth
import com.victormagosso.vfood.config.FirebaseConfig


class UserFirebaseData {
    var firebaseConfig = FirebaseConfig()
    var base64Custom = Base64Custom()
    fun getUid(): String? {
        val user: FirebaseAuth = firebaseConfig.getFirebaseAuth()
        return base64Custom.encodeToBase64(user.currentUser!!.email.toString())
    }
}