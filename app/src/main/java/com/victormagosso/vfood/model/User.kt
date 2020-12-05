package com.victormagosso.vfood.model

import com.google.firebase.database.Exclude

class User {
    @get:Exclude
    var cId: String? = "";

    var cName: String? = "";

    var dDoc: String? = "";

    var cType: String? = "";

    var cEmail: String? = "";

    var dDate: String = "";


}