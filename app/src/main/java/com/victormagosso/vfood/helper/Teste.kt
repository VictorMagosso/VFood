package com.victormagosso.vfood.helper

import com.victormagosso.vfood.config.FirebaseConfig

class Teste {
    var firebaseConfig = FirebaseConfig()

    fun getData() {
        firebaseConfig.getFirebaseDatabase().parent
    }
}