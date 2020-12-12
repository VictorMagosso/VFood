package com.victormagosso.vfood.model.client

import com.google.firebase.database.Exclude

class Client {
    @get:Exclude
    var cId: String? = "";

    var cClientName: String? = "";

    var cType: String? = "U";

    var dDate: String = "";

    var cTime: String = "";

    var cEmail: String = "";
}