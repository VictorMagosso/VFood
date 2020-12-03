package com.victormagosso.vfood.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.victormagosso.vfood.config.FirebaseConfig
import java.lang.Exception

class UserFirebase {
    val firebaseConfig = FirebaseConfig()
    fun getUID(): String? {
        val auth: FirebaseAuth = firebaseConfig.getFirebaseAuth()
        return auth.currentUser?.uid
    }
    fun getCurrentUser(): FirebaseUser {
        val currentUser = firebaseConfig.getFirebaseAuth().currentUser
        return currentUser!!
    }
    fun setUserType(type: String): Boolean {

        return try {
            var user: FirebaseUser = getCurrentUser()
            var userProfile = UserProfileChangeRequest.Builder()
                .setDisplayName(type)
                .build()
            user.updateProfile(userProfile)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}